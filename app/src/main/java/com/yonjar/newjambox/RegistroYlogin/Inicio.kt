package com.yonjar.newjambox.RegistroYlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yonjar.newjambox.R

class Inicio : AppCompatActivity() {
    private lateinit var btnIniciar:Button
    private lateinit var txtRegistrar:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        btnIniciar = findViewById(R.id.button)
        txtRegistrar = findViewById(R.id.txtRegistrate)

        btnIniciar.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

            txtRegistrar.setOnClickListener {
                val intent = Intent(this,Registro::class.java)
                startActivity(intent)
            }

    }
}