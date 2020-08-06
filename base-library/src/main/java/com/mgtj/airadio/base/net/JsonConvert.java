/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mgtj.airadio.base.net;

import androidx.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.mgtj.airadio.base.utils.json.GsonConvert;
import com.mgtj.airadio.base.net.data.DataResponse;
import com.mgtj.airadio.base.net.data.SimpleResponse;
import com.mgtj.airadio.base.net.exception.BusinessException;
import com.mgtj.airadio.base.net.exception.ParserException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback


        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, @NonNull Class<?> rawType) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = null;
        try {
            if (rawType == String.class) {
                //noinspection unchecked
                return (T) body.string();
            } else if (rawType == JSONObject.class) {
                //noinspection unchecked
                return (T) new JSONObject(body.string());
            } else if (rawType == JSONArray.class) {
                //noinspection unchecked
                return (T) new JSONArray(body.string());
            } else {
                jsonReader = new JsonReader(body.charStream());
                T t = GsonConvert.fromJson(jsonReader, rawType);
                response.close();
                return t;
            }

        } catch (Exception e) {
            throw new ParserException(e);
        } finally {
            if (null != jsonReader) {
                try {
                    jsonReader.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private T parseType(Response response, @NonNull Type type) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new TraderJsonCallback<任意JavaBean>(this)
        T t = GsonConvert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, @NonNull ParameterizedType type) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数

        String url = response.request().url().toString();
//        IMSLogger.d("url-->" + url);

        JsonReader jsonReader = new JsonReader(body.charStream());
        if (rawType != DataResponse.class) {
            // 泛型格式如下： new TraderJsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = GsonConvert.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new TraderJsonCallback<DataResponse<Void>>(this)
                SimpleResponse simpleResponse = GsonConvert.fromJson(jsonReader, SimpleResponse.class);
                response.close();
                if (null == simpleResponse) {
                    throw new ParserException();
                }
                //noinspection unchecked
                return handleDataResponse(simpleResponse.toDataResponse(), url);
            } else {
                // 泛型格式如下： new TraderJsonCallback<DataResponse<内层JavaBean>>(this)
                DataResponse lzyResponse = GsonConvert.fromJson(jsonReader, type);
                response.close();
                return handleDataResponse(lzyResponse, url);
            }
        }
    }

    private T handleDataResponse(DataResponse lzyResponse, String url) throws BusinessException {
        if (null == lzyResponse) {
            throw new BusinessException("Data Error!", BusinessException.NoDataError);
        }
        String code = lzyResponse.code;
        String message = lzyResponse.message;
        //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
        if ("100".equals(code)) {
            //noinspection unchecked
            return (T) lzyResponse;
        }
        //403表示token失效
//        else if ("403".equals(code)) {
//            throw new FdzqApiException(message, FdzqApiException.AuthError);
//        }
        else {
            //直接将服务端的错误信息抛出，onError中可以获取
            throw new BusinessException(message, BusinessException.BusinessError);
        }
    }

}
