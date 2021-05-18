package kz.tengrilab.starline

import com.google.gson.JsonObject
import kz.tengrilab.starline.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Multipart
    //@Headers("Accept: application/json; version=1.0")
    @POST("mobile/mobile_identification/")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part personId: MultipartBody.Part,
        @Part threshold: MultipartBody.Part
    ): Call<ResponseBody>

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
    ) : Call<AppUserInfo>


    @Headers("Content-Type: application/json")
    @POST("json/v1/device/41233995/set_param")
    fun unBlockCar(
        @Header("Cookie") cookie: String,
        @Body body: DataArm
    ) : Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST("json/v1/device/41233995/set_param")
    fun engineCar(
        @Header("Cookie") cookie: String,
        @Body body: DataIgn
    ) : Call<ResponseBody>


    @GET("json/device/41233995/ctrls_library")
    fun getLibrary() : Call<JsonObject>
}