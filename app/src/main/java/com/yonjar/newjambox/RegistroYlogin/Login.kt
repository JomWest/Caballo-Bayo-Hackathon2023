package com.yonjar.newjambox.RegistroYlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.yonjar.newjambox.MainActivity
import com.yonjar.newjambox.R

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnIngresar: Button
    private lateinit var tvRegistrate:TextView

    private var email:String = ""
    private var passsword:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        initComponents()

        btnIngresar.setOnClickListener {

            ObtenerVariables()

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edEmail.error = "Ingrese una dirección de correo electrónico válida"
                return@setOnClickListener
            }
                if(passsword == "") {
                    edPassword.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener

            }
            auth.signInWithEmailAndPassword(email,passsword).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"Ingresó con éxito", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(this,"Hubo un error al ingresar, intente de nuevo", Toast.LENGTH_SHORT).show()

                }
            }
        }

        tvRegistrate.setOnClickListener {
            val intent = Intent(this,Registro::class.java)
            startActivity(intent)
        }

    }

    private fun initComponents() {
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)

        btnIngresar = findViewById(R.id.btnIngresar)
        tvRegistrate = findViewById(R.id.tvRegistrate)
    }

    private fun ObtenerVariables(){
        email = edEmail.text.toString()
        passsword = edPassword.text.toString()
    }

}