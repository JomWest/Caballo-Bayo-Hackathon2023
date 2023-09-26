package com.yonjar.newjambox

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnEditarDesc: Button
    private lateinit var btnAceptarDesc: Button
    private lateinit var btnCancelDesc: Button

    private lateinit var tvNameProfile: TextView
    private lateinit var tvDesc: TextView
    private lateinit var etDesc: EditText
    private lateinit var etArtName: EditText
    private lateinit var ivProfile: ImageView


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri -> if(uri!=null){
        ivProfile.setImageURI(uri)
    }
    else{
        println("No se selecciono nada")
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        initComponents(view)

        btnEditarDesc.setOnClickListener {
            println("Hello World 3")
            showDialog()
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
        btnEditarDesc = view.findViewById(R.id.btnEditarDesc)
        tvNameProfile = view.findViewById(R.id.TVprofileName)
        tvDesc = view.findViewById(R.id.TVdesc)
        ivProfile = view.findViewById(R.id.IVprofile)
    }


    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_desc)

        btnAceptarDesc = dialog.findViewById(R.id.btnAceptarDesc)
        btnCancelDesc = dialog.findViewById(R.id.btnCancelDesc)
        etDesc = dialog.findViewById(R.id.etDesc)
        etArtName = dialog.findViewById(R.id.etArtName)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        btnAceptarDesc.setOnClickListener {
            if (etDesc.text.toString().isEmpty() || etArtName.text.toString().isEmpty()) {
                return@setOnClickListener
            }
            tvDesc.text = etDesc.text.toString().trim()
            tvNameProfile.text = etArtName.text.toString().trim()

            dialog.dismiss()
        }
        btnCancelDesc.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}