package com.yonjar.njambox

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnEditar: TextView
    private lateinit var btnAceptarDesc: Button
    private lateinit var btnCancelDesc: Button
    private lateinit var btnAgregarCanc:FloatingActionButton

    private lateinit var btnSubirAudio:Button
    private lateinit var btnAceptarCanc:Button
    private lateinit var btnCancelarCanc:Button
    private lateinit var tvAudio:TextView
    private lateinit var ivCover:ImageView
    private lateinit var etNombreCanc:TextView

    private lateinit var tvNameProfile: TextView
    private lateinit var tvDescripcion: TextView

    private lateinit var etArtName: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var ivProfile: ImageView

    private var emailActual:String? =  null
    private var userActual:String? = null
    private var userIdActual:String? = null
    private var profileImage:String? = null
    private var descripcion:String? = null

    val REQUEST_AUDIO_FILE = 1


    private lateinit var database: FirebaseDatabase

    val pickMediaCover = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
        if(uri != null){
            ivCover.setImageURI(uri)
        }else{

        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri -> if(uri!=null){
        ivProfile.setImageURI(uri)

        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/${userIdActual}/profile.jpg")

        val uri = imageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val imageUrl = downloadUri.toString()

                val userRef = database.getReference("users")
                val usuarioActualizar = userRef.child(userIdActual!!)

                val actualizacion = hashMapOf<String,Any>(
                    "profileImage" to imageUrl
                )

                usuarioActualizar.updateChildren(actualizacion).addOnSuccessListener {
                    Toast.makeText(requireContext(),"Actualización exitosa",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(),"Se produjo un error",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    else{
        Toast.makeText(requireContext(),"Actualización cancelada",Toast.LENGTH_SHORT).show()
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityIntent = requireActivity().intent
        if (activityIntent != null){
        emailActual = activityIntent.extras?.getString("email")
            userActual = activityIntent.extras?.getString("userName")
            userIdActual = activityIntent.extras?.getString("userId")
            profileImage = activityIntent.extras?.getString("profileImage")
            descripcion = activityIntent.extras?.getString("descripcion")

        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val mainActivity = activity as? MainActivity
        mainActivity?.let {
            val tvWelcome = it.tvWelcome

            tvWelcome?.visibility = View.INVISIBLE
        }

        database = FirebaseDatabase.getInstance()

        initComponents(view)

        tvNameProfile.text = userActual
        tvDescripcion.text = descripcion

        Picasso.get().load(profileImage).into(ivProfile)

        btnEditar.setOnClickListener {
            showDialog()
        }

        btnAgregarCanc.setOnClickListener {
            showDialogSong()
        }

        ivProfile.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        return view
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initComponents(view: View) {
        btnEditar = view.findViewById(R.id.btnEditar)
        btnAgregarCanc = view.findViewById(R.id.btnAgregarCanc)
        tvNameProfile = view.findViewById(R.id.TVprofileName)
        tvDescripcion = view.findViewById(R.id.TVDescripcion)

        ivProfile = view.findViewById(R.id.IVprofile)



    }


    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_desc)

        btnAceptarDesc = dialog.findViewById(R.id.btnAceptarDesc)
        btnCancelDesc = dialog.findViewById(R.id.btnCancelDesc)
        etArtName = dialog.findViewById(R.id.etArtName)
        etDescripcion = dialog.findViewById(R.id.etDescripcion)




        if (userActual != null){
            val editableNombre = Editable.Factory.getInstance().newEditable(userActual)
            etArtName.text = editableNombre

            val editableDesc = Editable.Factory.getInstance().newEditable(tvDescripcion.text.toString().trim())
            etDescripcion.text = editableDesc

        }else {return}

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        btnAceptarDesc.setOnClickListener {
            if (etArtName.text.toString().isEmpty()) {
                etArtName.error = "Este campo no puede quedar vacío"
                return@setOnClickListener
            }

            val userRef = database.getReference("users")
            val usuarioActualizar = userRef.child(userIdActual!!)

            val actualizacionUsername = hashMapOf<String,Any>(
                "userName" to etArtName.text.toString().trim(),
                "descripcion" to etDescripcion.text.toString().trim()

            )

            usuarioActualizar.updateChildren(actualizacionUsername).addOnSuccessListener {
                Toast.makeText(requireContext(),"Actualización exitosa",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Se produjo un error",Toast.LENGTH_SHORT).show()

            }


            tvNameProfile.text = etArtName.text.toString().trim()
            tvDescripcion.text = etDescripcion.text.toString().trim()

            dialog.dismiss()
        }
        btnCancelDesc.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun showDialogSong(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_canciones)

        btnCancelarCanc = dialog.findViewById(R.id.btnCancelCanc)
        btnAceptarCanc = dialog.findViewById(R.id.btnAceptarCanc)

        btnSubirAudio = dialog.findViewById(R.id.btnSubirAudio)
        tvAudio = dialog.findViewById(R.id.tvAudio)
        etNombreCanc = dialog.findViewById(R.id.etCancNombre)
        ivCover = dialog.findViewById(R.id.ivCover)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)




        ivCover.setOnClickListener {

            pickMediaCover.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnAceptarCanc.setOnClickListener {
            if (etNombreCanc.text.toString().isEmpty()) {
                etNombreCanc.error = "Este campo no puede quedar vacío"
                return@setOnClickListener
            }

            dialog.dismiss()
        }

        btnCancelarCanc.setOnClickListener {
            dialog.dismiss()
        }

        btnSubirAudio.setOnClickListener {
            selectAudioFile(requireActivity())
        }

        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_AUDIO_FILE && resultCode == Activity.RESULT_OK) {
            val audioUri: Uri? = data?.data
        }
    }

    fun selectAudioFile(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
        }

        activity.startActivityForResult(intent, REQUEST_AUDIO_FILE)
    }


}


