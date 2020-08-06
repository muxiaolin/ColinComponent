package com.mgtj.airadio.base.utils.deviceinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.core.os.EnvironmentCompat;

import com.blankj.utilcode.util.NetworkUtils;
import com.mgtj.airadio.base.LibInitApp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * @author PengLin
 * @project robo
 * @desc
 * @date 2020/4/14
 */
public class TelPhoneUtils {

    private static final String TAG = TelPhoneUtils.class.getSimpleName();

    /**
     * 判断数据流量开关是否打开
     *
     * @return
     */
    public static boolean isMobileDataEnabled() {
        return NetworkUtils.getMobileDataEnabled();
//        try {
//            Method method = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
//            method.setAccessible(true);
//            ConnectivityManager connectivityManager = (ConnectivityManager) InitApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            return (Boolean) method.invoke(connectivityManager);
//        } catch (Throwable t) {
//            Log.e(TAG, "Check mobile data encountered exception");
//            return false;
//        }
    }



    /**
     * 获取手机号码
     * <p>
     * 需要动态获取android.permission.READ_PHONE_STATE 权限
     *
     * @return
     */
    @RequiresPermission(READ_PHONE_STATE)
    public static String getTelPhone() {
        TelephonyManager tm = (TelephonyManager) LibInitApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = tm.getSimSerialNumber();//获得SIM卡的序号
//        String imsi = tm.getSubscriberId();//得到用户Id
//            String deviceid = tm.getDeviceId();//获取智能设备唯一编号
        String telPhone = tm.getLine1Number();//获取本机号码
        Log.d(TAG, "telPhone: " + telPhone);
        if (!TextUtils.isEmpty(telPhone)) {
            return telPhone;
        }

        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    //在Android6.0以及之后，需要动态获取android.permission.READ_PHONE_STATE 权限。因此存在用户拒绝授权的可能，此外首次启动后就上报设备ID时也可能影响启动速度
    //可能存在获取不到DeviceId的可能，存在返回null或者000000的垃圾数据可能
    //只对有电话功能的设备有效(无需插卡，但是需要有对应硬件模块)。例如在部分pad上可能无法获取到DeviceId
    @RequiresPermission(READ_PHONE_STATE)
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        try {
            TelephonyManager tm = (TelephonyManager) LibInitApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 运营商网络信号强度
     *
     * @return
     */
    @RequiresPermission(READ_PHONE_STATE)
    @SuppressLint("MissingPermission")
    public static int getDbm() {
        try {
            TelephonyManager tm = (TelephonyManager) LibInitApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            List<CellInfo> allCellInfo = tm.getAllCellInfo();
            int i = -1;
            if (allCellInfo != null) {
                /** 1、GSM是通用的移动联通电信2G的基站。
                 2、CDMA是3G的基站。
                 3、LTE，则证明支持4G的基站。*/
                for (CellInfo next : allCellInfo) {
                    if (next instanceof CellInfoGsm) {
                        i = ((CellInfoGsm) next).getCellSignalStrength().getDbm();
                    } else if (next instanceof CellInfoCdma) {
                        i = ((CellInfoCdma) next).getCellSignalStrength().getDbm();
                    } else if (next instanceof CellInfoWcdma) {
                        i = ((CellInfoWcdma) next).getCellSignalStrength().getDbm();
                    } else if (next instanceof CellInfoLte) {
                        i = ((CellInfoLte) next).getCellSignalStrength().getDbm();
                    }
                }
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    /**
     * 获取运营商sim卡的ICCID号
     * <p>
     * 需要动态获取android.permission.READ_PHONE_STATE 权限
     *
     * @return ICCID号
     */
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    @SuppressLint("MissingPermission")
    public static String getICCID() {
        Context context = LibInitApp.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> infoList;
            try {
                if ((infoList = SubscriptionManager.from(context).getActiveSubscriptionInfoList()) != null
                        && infoList.size() >= 1) {
                    return infoList.get(0).getIccId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(simSerialNumber)) {
                return tm.getSimSerialNumber();
            }
        }
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

}
