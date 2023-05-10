package com.motionProject.ble.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.motionProject.ble.R
import com.motionProject.ble.databinding.ActivityMainBinding
import com.motionProject.ble.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SensingActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensing)
//        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
//            this,
//            R.layout.activity_sensing
//        )

//        binding.viewModel = viewModel


        init()
        callEvent()
        msgEvent()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    interface onBackPressedListener {
        fun onBackPressed()
    }

    // call Event를 여기에 설정해야, 심박수가 특정 값이 됐을 때 전화걸기가 가능함.
    // msg Event도 마찬가지
    fun init() {
        var hr_value = findViewById<TextView>(R.id.hr_value)
        var permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                var phoneNumber = "010-6577-7996"
                var myUri = Uri.parse("tel:${phoneNumber}")
                var call_intent = Intent(Intent.ACTION_DIAL, myUri)
                if (hr_value.text == "10") {
                    startActivity(call_intent)
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                Toast.makeText(this@MainActivity, "전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("[설정] 에서 권한을 열어줘야 전화 연결이 가능합니다.")
            .setPermissions(Manifest.permission.CALL_PHONE)
            .check()
    }
    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
//        var fragmentList = supportFragmentManager.fragments
//        for (fragment in fragmentList) {
//            if (fragment is onBackPressedListener) {
//                (fragment as onBackPressedListener).onBackPressed()
//                return
//            }
//        }
//        var first_pressed_time = 0L
//        if (System.currentTimeMillis() - first_pressed_time >= 1500) {
//            first_pressed_time = System.currentTimeMillis()
//            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
//        } else {
//            finishAffinity()
//        }
    }

    fun callEvent() {
        var callBtn = findViewById<AppCompatImageButton>(R.id.call_btn)

        callBtn.setOnClickListener {
            var phoneNumber = "119"
            var myUri = Uri.parse("tel:" + phoneNumber)
            var call_intent = Intent(Intent.ACTION_CALL, myUri)
            startActivity(call_intent)
//            var permissionListener = object : PermissionListener {
//                override fun onPermissionGranted() {
//                    var myUri = Uri.parse("tel:" + phoneNumber)
//                    var call_intent = Intent(Intent.ACTION_CALL, myUri)
//                    startActivity(call_intent)
//                }
//
//                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                    Toast.makeText(this@MainActivity, "전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }

        }
    }

    fun msgEvent() {
        var msgBtn = findViewById<AppCompatImageButton>(R.id.msg_btn)

        msgBtn.setOnClickListener {
            // Intent - 문자 내용까지만 입력되고, 전송 버튼은 사용자가 눌러야 함
            var phoneNum = "119"
            var myUri = Uri.parse("smsto:${phoneNum}")
            var msg_intent = Intent(Intent.ACTION_SENDTO, myUri)

            msg_intent.putExtra("sms_body", "낙상 사고 발생. 도움 요청!")
            startActivity(msg_intent)

            // smsManager - 문자 자동 전송
            var smsManger = SmsManager.getDefault()
            smsManger.sendTextMessage("119", null, "도와주세요.", null, null)
//            Toast.makeText(this@MainActivity, "전송 완료", Toast.LENGTH_SHORT).show()
        }
    }

//    fun initTransactionEvent() {
//        var hrBtn = findViewById<AppCompatButton>(R.id.hr_btn)
//        var tempBtn = findViewById<AppCompatButton>(R.id.temp_btn)
//        var respBtn = findViewById<AppCompatButton>(R.id.resp_btn)
//
//        hrBtn.setOnClickListener {
//            var hrFragment = HrFragment()
//            var manager = supportFragmentManager
//            var transaction = manager.beginTransaction()
//            transaction.replace(R.id.fragment_container, hrFragment).commit()
//        }
//
//        tempBtn.setOnClickListener {
//            var tempFragment = TempFragment()
//            var manager = supportFragmentManager
//            var transaction = manager.beginTransaction()
//            transaction.replace(R.id.fragment_container, tempFragment).commit()
//        }
//
//        respBtn.setOnClickListener {
//            var respFragment = RespFragment()
//            var manager = supportFragmentManager
//            var transaction = manager.beginTransaction()
//            transaction.replace(R.id.fragment_container, respFragment).commit()
//        }
//    }
}