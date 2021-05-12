package kz.tengrilab.starline.models

data class AppToken(
    val desc: Desc,
    val state: Int
) {
    data class Desc(
        val token: String
    )
}