package kz.tengrilab.starline.models

data class AppInfo(
    val code: Int,
    val codestring: String,
    val devices: List<Any>,
    val shared_devices: List<SharedDevice>
) {
    data class SharedDevice(
        val alias: String,
        val battery: Int,
        val device_id: String,
        val fw_version: Any,
        val gps_lvl: Int,
        val imei: String,
        val interval: Int,
        val mayak_temp: Int,
        val mon_type: Int,
        val phone: String,
        val position: Position,
        val reg: Any,
        val roles: List<String>,
        val rpl_channel: Any,
        val sn: Any,
        val status: Int,
        val ts_activity: Int,
        val ts_wakeup: Int,
        val type: Int
    ) {
        data class Position(
            val dir: Any,
            val s: Any,
            val sat_qty: Int,
            val ts: Int,
            val x: String,
            val y: String
        )
    }
}