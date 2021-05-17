package kz.tengrilab.starline.models

import com.google.gson.annotations.SerializedName

data class DataIgn(
        @SerializedName("type")
        val type: String,
        @SerializedName("ign")
        val ign: Int
)
