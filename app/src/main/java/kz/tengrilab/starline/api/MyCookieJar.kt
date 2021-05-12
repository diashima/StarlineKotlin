package kz.tengrilab.starline.api

import android.util.Log
import kz.tengrilab.starline.Variables
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class MyCookieJar : CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookiesList: MutableList<Cookie>) {
        cookies.clear()
        cookies.addAll(cookiesList)
        val substring = cookiesList.toString().substringAfter("slnet=").substringBefore(";")
        Variables.cookie = "slnet=$substring"
        Log.d("Test", Variables.cookie)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        cookies.clear()
        return cookies
    }
}