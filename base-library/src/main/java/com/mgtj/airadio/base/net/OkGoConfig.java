package com.mgtj.airadio.base.net;

import android.app.Application;

import androidx.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.mgtj.airadio.base.LibInitApp;

import org.joda.time.Period;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * author : 彭林
 * date   : 2020/7/24
 * desc   :
 */
public class OkGoConfig {
    private Builder configBuilder;

    private OkGoConfig(@NonNull Builder builder) {
        configBuilder = builder;
    }

    /**
     * OKGo配置
     */
    public void okGoConfig() {
        boolean logDebug = LibInitApp.isDebug();
        Application context = LibInitApp.getApplication();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //配置log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("MT_okGo");
        loggingInterceptor.setPrintLevel(logDebug ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //配置超时时间
        builder.connectTimeout(configBuilder.connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(configBuilder.readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(configBuilder.writeTimeout, TimeUnit.SECONDS);

        //配置Cookie（或者叫session的保持），以下几种任选其一就行
        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(context)));
        //使用内存保持cookie，app退出后，cookie消失
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //Https配置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//            HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        //设置拦截器
//        builder.addInterceptor(mInterceptor);
//        builder.authenticator(new TokenAuthenticator());

//        long cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        long cacheTime = Period.months(1).getMillis(); //1个月
        // 配置OkGo
        OkGo.getInstance().init(context)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.DEFAULT)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(cacheTime)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(configBuilder.retryCount)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(configBuilder.httpHeaders)                      //全局公共头
                .addCommonParams(configBuilder.httpParams)                        //全局公共参数
        ;
    }

    public static final class Builder {
        int connectTimeout = 5;
        int readTimeout = 15;
        int writeTimeout = 15;
        int retryCount = 2;
        HttpHeaders httpHeaders;
        HttpParams httpParams;

        public OkGoConfig build() {
            return new OkGoConfig(this);
        }

        public Builder connectTimeout(int timeout) {
            connectTimeout = timeout;
            return this;
        }


        public Builder readTimeout(int timeout) {
            readTimeout = timeout;
            return this;
        }

        public Builder writeTimeout(int timeout) {
            writeTimeout = timeout;
            return this;
        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }


        public Builder setHttpHeaders(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public Builder setHttpParams(HttpParams httpParams) {
            this.httpParams = httpParams;
            return this;
        }
    }


}
