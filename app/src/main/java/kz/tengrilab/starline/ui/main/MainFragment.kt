package kz.tengrilab.starline.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import kz.tengrilab.starline.ApiClient
import kz.tengrilab.starline.ApiInterface
import kz.tengrilab.starline.Variables
import kz.tengrilab.starline.databinding.FragmentMainBinding
import kz.tengrilab.starline.models.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitId = ApiClient.getRetrofitIdClient()
        val loginInterface = retrofitId.create(ApiInterface::class.java)

        val retrofitDeveloper = ApiClient.getRetrofitDeveloperClient()
        val developerInterface = retrofitDeveloper.create(ApiInterface::class.java)

        binding.button.setOnClickListener {
            val secret = md5(Variables.secret)
            val call = loginInterface.getAppCode(Variables.appId, secret)
            call.enqueue(object : Callback<AppCode> {
                override fun onResponse(call: Call<AppCode>, response: Response<AppCode>) {
                    Log.d("Test", response.body().toString())
                    val answer = response.body()!!.desc.code
                    //Toast.makeText(requireContext(), code, Toast.LENGTH_LONG).show()
                    Variables.secret_app = answer
                    Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                    Log.d("Test", Variables.secret_app)
                }

                override fun onFailure(call: Call<AppCode>, t: Throwable) {
                    Toast.makeText(requireContext(), "GET_APP_CODE FAILED", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

        binding.button2.setOnClickListener {
            val concat = Variables.secret + Variables.secret_app
            val secret = md5(concat)
            val call = loginInterface.getAppToken(Variables.appId, secret)
            call.enqueue(object : Callback<AppToken> {
                override fun onResponse(call: Call<AppToken>, response: Response<AppToken>) {
                    Log.d("Test", response.body().toString())
                    val answer = response.body()!!.desc.token
                    Variables.token = answer
                    Log.d("Test", Variables.token)
                    Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<AppToken>, t: Throwable) {
                    Toast.makeText(requireContext(), "GET_APP_TOKEN FAILED", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

        binding.button3.setOnClickListener {
            //val part1 = MultipartBody.Part.createFormData("login", Variables.login)
            //val part2 = MultipartBody.Part.createFormData("pass", Variables.pass)
            val call = loginInterface.getSlidUserToken(
                Variables.token,
                Variables.login,
                Variables.pass
            )
            call.enqueue(object : Callback<AppLogin> {
                override fun onResponse(call: Call<AppLogin>, response: Response<AppLogin>) {
                    Log.d("Test", response.body().toString())
                    val answer = response.body()!!.desc.user_token
                    Variables.slid_token = answer
                    Log.d("Test", Variables.slid_token)

                }

                override fun onFailure(call: Call<AppLogin>, t: Throwable) {
                    Toast.makeText(requireContext(), "GET_APP_SLID_User failed", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

        binding.button4.setOnClickListener {
            val requestBody: HashMap<String, String> = HashMap()
            requestBody["slid_token"] = Variables.slid_token
            val call = developerInterface.getSlnetToken(requestBody)
            call.enqueue(object : Callback<AppSlnet> {
                override fun onResponse(call: Call<AppSlnet>, response: Response<AppSlnet>) {
                    Variables.userId = response.body()!!.user_id
                    Log.d("Test", response.body()!!.toString())
                }

                override fun onFailure(call: Call<AppSlnet>, t: Throwable) {
                    Toast.makeText(requireContext(), "GET_APP_SLNET failed", Toast.LENGTH_LONG)
                        .show()
                }

            })
        }

        binding.button5.setOnClickListener {
            val call = developerInterface.getUserInfo(Variables.cookie, Variables.userId)
            call.enqueue(object : Callback<AppUserInfo> {
                override fun onResponse(call: Call<AppUserInfo>, response: Response<AppUserInfo>) {
                    val lat = response.body()!!.devices[0].position.x
                    Log.d("Test", lat.toString() )
                    MainFragmentDirections.startMapFr(response.body()!!.devices[0].position.x.toString(), response.body()!!.devices[0].position.y.toString()).apply {
                        findNavController().navigate(this)
                    }
                }

                override fun onFailure(call: Call<AppUserInfo>, t: Throwable) {
                    Toast.makeText(requireContext(), "GET_USER_INFO failed", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

        /*binding.button6.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("type", "arm")
            jsonObject.put("arm", "0")

            val requestBody: HashMap<String, String> = HashMap()
            requestBody["type"] = "arm"
            requestBody["arm"] = "0"
            val dataArm = DataArm("ign", 1)
            val kek = "{type: arm, arm: 0}"
            Log.d("Test", dataArm.toString())


            val call = developerInterface.unBlockCar(Variables.cookie, dataArm)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val finish = response.body()!!.string()
                        Log.d("Test", finish)
                    } else {
                        val objError = JSONObject(response.errorBody().toString())
                        Log.d("Test", objError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("Test", "fail")
                }
            })
        }*/

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),0
            )}
    }


    private fun md5(input: String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}