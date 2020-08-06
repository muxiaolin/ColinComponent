package com.mgtj.airadio.base.utils.deviceinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author PengLin
 * @project robo
 * @desc
 * @date 2020/4/16
 */
public class DeviceInfoUtil {


    /**
     * 获取设备唯一标识, 需要获得READ_PHONE_STATE权限
     * <p>
     * 缺点：
     * （1）系统刷机或者恢复出厂后就是新设备ID，对于恢复出厂和刷机都属于极端操作，概率很低
     * （2）极端情况下IMI、AndroidID或者SerialNumber均获取失败或者多个设备获取到的都是相同值，那么就会出现多台设备是同一Deviceid，但是这样的概率很低
     *
     * @param context 上下文
     * @return 设备硬件标识
     */
    public static String getDeviceId(Context context) {
        StringBuilder sbDeviceId = new StringBuilder();

        //获得设备默认IMEI（>=6.0 需要ReadPhoneState权限）
        String imei = getIMEI(context);
        //获得AndroidId（无需权限）
        String androidid = getAndroidId(context);
        //获得设备序列号（无需权限）
        String serial = getSERIAL_1();
        //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）
        String uuid = getDeviceUUID().replace("-", "");

        //追加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //追加androidid
        if (androidid != null && androidid.length() > 0) {
            sbDeviceId.append(androidid);
            sbDeviceId.append("|");
        }
        //追加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //追加硬件uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }

        //生成SHA1，统一DeviceId长度
        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = getHashByString(sbDeviceId.toString());
                String sha1 = bytesToHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //如果以上硬件标识数据均无法获得，
        //则DeviceId默认使用系统随机数，这样保证DeviceId不为空
        return UUID.randomUUID().toString().replace("-", "");
    }

    //2）缺点
    //在Android6.0以及之后，需要动态获取android.permission.READ_PHONE_STATE 权限。因此存在用户拒绝授权的可能，此外首次启动后就上报设备ID时也可能影响启动速度
    //可能存在获取不到DeviceId的可能，存在返回null或者000000的垃圾数据可能
    //只对有电话功能的设备有效(无需插卡，但是需要有对应硬件模块)。例如在部分pad上可能无法获取到DeviceId
    @SuppressLint("MissingPermission")
    private static String getIMEI(Context context) {
        return TelPhoneUtils.getIMEI();
    }

    /**
     * 设备首次启动后系统会随机生成一个64位的数字，用16进制字符串的形式表示，例如：4351daa4516303b3，4351 daa4 5163 03b3
     * 获得设备的AndroidId
     * <p>
     * 2）缺点
     * 恢复出厂或者刷机后会被重置
     * 部分厂商定制系统中，可能为空，也可能是不同设备中会产生相同的值
     * 对于CDMA设备汇总，AndroidId和DeviceId会返回相同的值
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     * <p>
     * （2）缺点
     * 部分设备无法获取到
     * 部分红米手机都会返回无用值0123456789ABCDEF
     *
     * @return 设备序列号
     */
    public static String getSERIAL_1() {
        try {
            String serialNumber = null;
            try {
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                if (clazz != null) {
                    Method method_get = clazz.getMethod("get", String.class, String.class);
                    if (method_get != null) {
                        serialNumber = (String) (method_get.invoke(clazz, "ro.serialno", ""));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return serialNumber != null ? serialNumber : "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * @return
     */
    public static String getSERIAL_2() {
        try {
            return Build.class.getField("SERIAL").get((Object) null).toString();
        } catch (Exception e) {
            return Build.SERIAL;
        }
    }

    /**
     * 获得设备硬件uuid
     * 使用硬件信息，计算出一个随机数
     *
     * @return 设备硬件uuid
     */
    private static String getDeviceUUID() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = (Integer.toHexString(data[n] & 0xFF));
            if (stmp.length() == 1)
                sb.append("0");
            sb.append(stmp);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }


    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getBRAND() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getSystemSModel() {
        return Build.MODEL;
    }


    /**
     * 获取系统SDK版本
     *
     * @return
     */
    public static int getSystemSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersionCode() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取CPU核数
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    //Check if filename is "cpu", followed by a single digit number
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    private static Object[] mArmArchitecture = new Object[3];

    /**
     * [获取cpu类型和架构]
     *
     * @return 三个参数类型的数组，第一个参数标识是不是ARM架构，
     * 第二个参数标识是V6还是V7架构，
     * 第三个参数标识是不是neon指令集
     */
    public static Object[] getCpuArchitecture() {
        if (mArmArchitecture != null && mArmArchitecture[1] instanceof Integer && (Integer) mArmArchitecture[1] != -1) {
            return mArmArchitecture;
        }
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                String nameProcessor = "Processor";
                String nameFeatures = "Features";
                String nameModel = "model name";
                String nameCpuFamily = "cpu family";
                while (true) {
                    String line = br.readLine();
                    String[] pair = null;
                    if (line == null) {
                        break;
                    }
                    pair = line.split(":");
                    if (pair.length != 2)
                        continue;
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareTo(nameProcessor) == 0) {
                        String n = "";
                        for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {
                            String temp = val.charAt(i) + "";
                            if (temp.matches("\\d")) {
                                n += temp;
                            } else {
                                break;
                            }
                        }
                        mArmArchitecture[0] = "ARM";
                        mArmArchitecture[1] = Integer.parseInt(n);
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameFeatures) == 0) {
                        if (val.contains("neon")) {
                            mArmArchitecture[2] = "neon";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameModel) == 0) {
                        if (val.contains("Intel")) {
                            mArmArchitecture[0] = "INTEL";
                            mArmArchitecture[2] = "atom";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameCpuFamily) == 0) {
                        mArmArchitecture[1] = Integer.parseInt(val);
                        continue;
                    }
                }
            } finally {
                br.close();
                ir.close();
                is.close();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return mArmArchitecture;
    }

}
