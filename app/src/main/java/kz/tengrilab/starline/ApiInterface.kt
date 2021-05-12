package kz.tengrilab.starline

import kz.tengrilab.starline.models.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("apiV3/application/getCode")
    fun getAppCode(
        @Query("appId") appId: String,
        @Query("secret") secret: String
    ) : Call<AppCode>


    @GET("apiV3/application/getToken")
    fun getAppToken(
        @Query("appId") appId: String,
        @Query("secret") secret_code: String
    ) : Call<AppToken>


    @FormUrlEncoded
    @POST("apiV3/user/login")
    fun getSlidUserToken(
        @Header("token") token: String,
        @Field("login") login: String,
        @Field("pass") pass: String
    ) : Call<AppLogin>


    @Headers("Content-Type: application/json")
    @POST("json/v2/auth.slid")
    fun getSlnetToken(
        @Body slidToken: Map<String, String>
    ) : Call<AppSlnet>


    @GET("json/v2/user/{id}/user_info")
    fun getUserInfo(
        @Header("Cookie") cookie: String,
        @Path("id") id: String
    ) : Call<AppInfo>


    @POST("json/v1/device/41233995/set_param")
    fun unBlockCar(
        @Header("Cookie") cookie: String,
        @Body setParam: Map<String, String>
    )
}