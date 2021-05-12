package kz.tengrilab.starline.models

data class AppLogin(
    val desc: Desc,
    val state: Int
) {
    data class Desc(
        val auth_contact_id: Any,
        val avatar: String,
        val company_name: String,
        val contacts: List<Contact>,
        val date_register: String,
        val first_name: String,
        val gmt: String,
        val id: String,
        val lang: String,
        val last_name: String,
        val login: String,
        val middle_name: String,
        val sex: Any,
        val state: String,
        val user_token: String
    ) {
        data class Contact(
            val confirmed: String,
            val id: String,
            val token: String,
            val type: String,
            val value: String
        )
    }
}