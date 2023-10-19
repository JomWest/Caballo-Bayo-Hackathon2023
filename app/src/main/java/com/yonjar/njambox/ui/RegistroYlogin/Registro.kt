package com.yonjar.njambox.RegistroYlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.QuerySnapshot
import com.yonjar.njambox.Classes.Usuarios
import com.yonjar.njambox.R
import java.security.MessageDigest
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
    var passwordHashed:String = ""

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


            auth.createUserWithEmailAndPassword(email, passwordHashed).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                val databaseRef = database.reference.child("users").child(user.uid)
                                val userObject = Usuarios(name, lastName, userName, email, passwordHashed, user.uid)

                                databaseRef.setValue(userObject).addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        btnRegistrar.isEnabled = false
                                        Toast.makeText(this, "Registro realizado con éxito", Toast.LENGTH_SHORT).show()
                                        Toast.makeText(this, "Le hemos enviado un correo electrónico para su verificación", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "Hubo un error al registrar, intente de nuevo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Hubo un error al enviar el correo de verificación, intente de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Hubo un error al registrar, intente de nuevo", Toast.LENGTH_SHORT).show()
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

        passwordHashed = hashPassword(password)

    }

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}