package kz.tengrilab.starline.models

import com.google.gson.annotations.SerializedName

data class DataArm(
        @SerializedName("type")
        val type: String,
        @SerializedName("arm")
        val arm: Int
)
