package kz.tengrilab.starline.models

data class AppCode(
    val desc: Desc,
    val state: Int
) {
    data class Desc(
        val code: String
    )
}