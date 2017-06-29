package com.vnetoo.test.api;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/6/29.
 * 请求网络的API接口类，这里你可以增加你需要的请求接口，也可复用已经实现的几个方法
 */

public interface ApiService {

    String Base_URL = "http://ip.taobao.com/";
    /**
     *普通写法
     */
    @GET("service/getIpInfo.php/")
    Observable<ResponseBody> getData(@Query("ip") String ip);

    @GET("{url}")
    Observable<ResponseBody> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> headers,
            @QueryMap Map<String, String> parameters);


    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @QueryMap Map<String, String> headers,
            @QueryMap Map<String, String> parameters);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\\\"; filename=\\\"image.jpg") RequestBody avatar);


    @POST("{url}")
    Observable<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap()  Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

//    上面新增了几个常用的请求方法
//    第一个只是普通写法的列子, url ,请求头，参数都是写死的。 不建议这么做
//    第二，三个分别是Get 和POST请求，method Url,headers, body参数都可以动态外部传入。
//    四和 五是单文件/图片和多文件/图片上传
//    最后是文件下载

//    如果你觉得麻烦 可以用T代替，技术不到位的悠着点哈
    @GET()
    <T> Observable<ResponseBody> executeGet2(
            @Url String url,
            @QueryMap Map<String, T> maps);
}
