package com.motionProject.ble.ui.main

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.motionProject.ble.PERMISSIONS
import com.motionProject.ble.R
import com.motionProject.ble.REQUEST_ALL_PERMISSION
import com.motionProject.ble.adapter.BleListAdapter
import com.motionProject.ble.databinding.ActivityMainBinding
import com.motionProject.ble.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private var adapter: BleListAdapter? = null
//    private lateinit var intent: Intent
    var phoneNumber = "119"
    var myUri = Uri.parse("tel:" + phoneNumber)
    var call_intent = Intent(Intent.ACTION_CALL, myUri)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        binding.viewModel = viewModel

        binding.rvBleList.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvBleList.layoutManager = layoutManager

        adapter = BleListAdapter()
        binding.rvBleList.adapter = adapter
        adapter?.setItemClickListener(object : BleListAdapter.ItemClickListener {
            override fun onClick(view: View, device: BluetoothDevice?) {
                if (device != null) {
                    viewModel.connectDevice(device)
                }
            }
        })

        // check if location permission
        if (!hasPermissions(this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION)
        }

        init()
        initObserver(binding)

        binding.btnHome.setOnClickListener {
            var intent = Intent(this, SensingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun init() {
//        var sensorData = findViewById<TextView>(R.id.sensor_data)
        var permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "전화 연결 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
//                var phoneNumber = "119"
//                var myUri = Uri.parse("tel:" + phoneNumber)
//                var call_intent = Intent(Intent.ACTION_CALL, myUri)
//                if (sensorData.text == "87") {
//                    startActivity(call_intent)
//                    var smsManger = SmsManager.getDefault()
//                    smsManger.sendTextMessage("119", null, "도와주세요.", null, null)
//                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("[설정] 에서 권한을 열어줘야 전화 연결이 가능합니다.")
            .setPermissions(Manifest.permission.CALL_PHONE)
            .check()
    }

    private fun initHomeBtn(){
        val homeBtn = findViewById<AppCompatButton>(R.id.btn_home)

    }

    private fun initObserver(binding: ActivityMainBinding){
        viewModel.requestEnableBLE.observe(this, {
            it.getContentIfNotHandled()?.let {
                requestEnableBLE()
            }
        })
        viewModel.listUpdate.observe(this, {
            it.getContentIfNotHandled()?.let { scanResults ->
                adapter?.setItem(scanResults)
            }
        })

        viewModel._isScanning.observe(this,{
            it.getContentIfNotHandled()?.let{ scanning->
                viewModel.isScanning.set(scanning)
            }
        })
        viewModel._isConnect.observe(this,{
            it.getContentIfNotHandled()?.let{ connect->
                viewModel.isConnect.set(connect)
            }
        })
        viewModel.statusTxt.observe(this,{

           binding.statusText.text = it

        })

        viewModel.readTxt.observe(this,{
            binding.txtRead.append(it)
            var text = it.substring(7).replace("[^0-9]".toRegex(), "")
            binding.sensorData.text = text
            Log.i("hwisik" , text.javaClass.name)
            // 데이터 전달

            if (text.equals("87")) {
                var smsManger = SmsManager.getDefault()
                smsManger.sendTextMessage("010-6577-7996", null, "도와주세요.", null, null)
                startActivity(call_intent)
            } else {
                Log.i("resultss", "None...")
            }

            Log.i("txtRead", it)

            if ((binding.txtRead.measuredHeight - binding.scroller.scrollY) <=
                (binding.scroller.height + binding.txtRead.lineHeight)) {
                binding.scroller.post {
                    binding.scroller.smoothScrollTo(0, binding.txtRead.bottom)
                }
            }

        })
    }
    override fun onResume() {
        super.onResume()
        // finish app if the BLE is not supported
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish()
        }
    }


    private val requestEnableBleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // do somthing after enableBleRequest
        }
    }

    /**
     * Request BLE enable
     */
    private fun requestEnableBLE() {
        val bleEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestEnableBleResult.launch(bleEnableIntent)
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }
    // Permission check
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ALL_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermissions(permissions, REQUEST_ALL_PERMISSION)
                    Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}