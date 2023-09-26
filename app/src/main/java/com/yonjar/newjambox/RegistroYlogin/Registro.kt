package com.yonjar.newjambox.RegistroYlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.yonjar.newjambox.Classes.Usuarios
import com.yonjar.newjambox.R
import java.util.Locale

class Registro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    //Declaracion de los controles para la interfaz de registro
    private lateinit var edName: EditText
    private lateinit var edLastName: EditText
    private lateinit var edUserName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var edPassword2: EditText
    private lateinit var btnRegistrar: Button



    //Declaracion de las variables sobre los datos necesarios para el registro
    var name:String = ""
    var lastName:String = ""
    var userName:String = ""
    var email:String = ""
    var password:String = ""
    var password2:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        initComponents()

        btnRegistrar.setOnClickListener {
            getVariables()


            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edEmail.error = "Ingrese una dirección de correo electrónico válida"
                return@setOnClickListener
            }
            when{
                name == "" -> { edName.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
                lastName == "" -> { edLastName.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
                userName == "" -> { edUserName.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
                email == "" -> { edEmail.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
                password == "" -> { edPassword.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
                password2 == "" -> { edPassword2.error = "Este campo no puede quedar vacío"
                    return@setOnClickListener }
            }
            if(password.length < 8 ){ edPassword.error = "Su contraseña debe contener almenos 8 caracteres"
                return@setOnClickListener }
            if(password != password2){ Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val databaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
                    val user:Usuarios = Usuarios(name,lastName,userName,email,password,auth.currentUser!!.uid)

                    databaseRef.setValue(user).addOnCompleteListener {
                        if (it.isSuccessful){

                            btnRegistrar.isEnabled = false
                            Toast.makeText(this,"Registro realizado con éxito", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this,Login::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,"Hubo un error al registrar, intente de nuevo",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this,"Hubo un error al registrar, intente de nuevo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initComponents() {
        edName = findViewById(R.id.edNombre)
        edLastName = findViewById(R.id.edApellido)
        edUserName = findViewById(R.id.edNombreUsuario)
        edEmail = findViewById(R.id.edCorreo)
        edPassword = findViewById(R.id.edContrasena)
        edPassword2 = findViewById(R.id.edContrasena2)
        btnRegistrar = findViewById(R.id.btnRegistrar)
    }

    private fun getVariables(){
        name = edName.text.toString()
        lastName = edLastName.text.toString()
        userName = edUserName.text.toString()
        email = edEmail.text.toString().lowercase(Locale.getDefault())
        password = edPassword.text.toString()
        password2 = edPassword2.text.toString()

    }
}