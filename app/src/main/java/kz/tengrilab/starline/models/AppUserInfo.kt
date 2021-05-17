package kz.tengrilab.starline.models

data class AppUserInfo(
    val code: Int,
    val codestring: String,
    val devices: List<Device>,
    val shared_devices: List<Any>
) {
    data class Device(
        val alias: String,
        val balance: Balance,
        val battery: Double,
        val car_alr_state: CarAlrState,
        val car_state: CarState,
        val controls: List<Control>,
        val ctemp: Int,
        val device_id: Int,
        val diag: Diag,
        val etemp: Int,
        val functions: List<String>,
        val fw_version: String,
        val gps_lvl: Int,
        val gsm_lvl: Int,
        val imei: String,
        val mon_type: Int,
        val phone: String,
        val position: Position,
        val reg_date: Int,
        val status: Int,
        val ts_activity: Int,
        val type: Int,
        val typename: String,
        val ua_url: String
    ) {
        data class Balance(
            val active: Active
        ) {
            data class Active(
                val currency: String,
                val `operator`: String,
                val state: String,
                val ts: String,
                val value: Int
            )
        }

        data class CarAlrState(
            val door: Boolean,
            val hbrake: Boolean,
            val hood: Boolean,
            val ign: Boolean,
            val pbrake: Boolean,
            val shock_h: Boolean,
            val shock_l: Boolean,
            val tilt: Boolean,
            val trunk: Boolean
        )

        data class CarState(
            val add_sens_bpass: Boolean,
            val alarm: Boolean,
            val arm: Boolean,
            val door: Boolean,
            val dvr: Boolean,
            val hbrake: Boolean,
            val hijack: Boolean,
            val hood: Boolean,
            val ign: Boolean,
            val `out`: Boolean,
            val r_start: Boolean,
            val run: Boolean,
            val shock_bpass: Boolean,
            val tilt_bpass: Boolean,
            val trunk: Boolean,
            val valet: Boolean,
            val webasto: Boolean
        )

        data class Control(
            val position: Int,
            val type: String
        )

        data class Diag(
            val can_descr: String,
            val can_version: String,
            val vin: String
        )

        data class Position(
            val r: Int,
            val sat_qty: Int,
            val ts: Int,
            val x: Double,
            val y: Double
        )
    }
}