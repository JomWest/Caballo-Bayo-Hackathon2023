package com.yonjar.njambox.RegistroYlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.yonjar.njambox.RegistroYlogin.Registro
import com.yonjar.njambox.Classes.Usuarios
import com.yonjar.njambox.MainActivity
import com.yonjar.njambox.R
import java.security.MessageDigest

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnIngresar: Button
    private lateinit var tvRegistrate:TextView
    private lateinit var ivOjo:ImageView

    private var email:String = ""
    private var passsword:String = ""
    private var userId:String = ""
    private var userName:String = ""
    private var visible:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

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

            val passwordHashed = hashPassword(passsword)

            auth.signInWithEmailAndPassword(email,passwordHashed).addOnCompleteListener {
                if(it.isSuccessful){

                    val user = auth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                            Toast.makeText(this,"Ingresó con éxito", Toast.LENGTH_SHORT).show()

                            val userRef = database.reference.child("users")
                            val intent = Intent(this,MainActivity::class.java)

                            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (userSnaphot in snapshot.children){
                                        val user = userSnaphot.getValue(Usuarios::class.java)

                                        if(user != null){

                                            userId = user.userId
                                            userName = user.userName
                                            intent.putExtra("name",user.name)
                                            intent.putExtra("email",email)
                                            intent.putExtra("userName",userName)
                                            intent.putExtra("userId",userId)
                                            intent.putExtra("profileImage",user.profileImage)
                                            intent.putExtra("descripcion",user.descripcion)
                                            startActivity(intent)
                                        }
                                    }

                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@Login,"Se ha producido un error",Toast.LENGTH_SHORT).show()

                                }
                            })
                        } else {
                            Toast.makeText(this, "Por favor, verifica tu correo electrónico antes de iniciar sesión", Toast.LENGTH_SHORT).show()
                            user?.sendEmailVerification()
                        }
                    }


                }else {
                    Toast.makeText(this,"Hubo un error al ingresar, intente de nuevo", Toast.LENGTH_SHORT).show()

                }
            }
        }

        tvRegistrate.setOnClickListener {
            val intent = Intent(this,Registro::class.java)
            startActivity(intent)
        }

        ivOjo.setOnClickListener {
            if(visible){
                ivOjo.setImageResource(R.drawable.visible)
                edPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                visible = false
            }
            else{
                ivOjo.setImageResource(R.drawable.ojo)
                edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                visible = true
            }
        }


    }

    private fun initComponents() {
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)

        btnIngresar = findViewById(R.id.btnIngresar)
        tvRegistrate = findViewById(R.id.tvRegistrate)
        ivOjo = findViewById(R.id.ivOjo)
    }

    private fun ObtenerVariables(){
        email = edEmail.text.toString()
        passsword = edPassword.text.toString()


    }

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }



}