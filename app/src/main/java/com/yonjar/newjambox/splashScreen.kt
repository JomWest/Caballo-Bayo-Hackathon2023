package com.yonjar.newjambox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.yonjar.newjambox.RegistroYlogin.Inicio
import com.yonjar.newjambox.RegistroYlogin.Login

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED
        )
        Handler(Looper.getMainLooper()).postDelayed({
          val intent = Intent(this,Inicio::class.java)
          startActivity(intent)
            finish()
        }, 550)
    }
}