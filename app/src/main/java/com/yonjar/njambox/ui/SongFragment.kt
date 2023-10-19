package com.yonjar.njambox

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yonjar.njambox.Classes.CancionesAdapter
import com.yonjar.njambox.MainActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SongFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var listener: FragmentListener? = null
    private var name:String? =  null


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

        val activityIntent = requireActivity().intent
        if (activityIntent != null){
            name = activityIntent.extras?.getString("name")
        }

        val mainActivity = activity as? MainActivity
        mainActivity?.let {
            val tvWelcome = it.tvWelcome

            tvWelcome.text = "Bienvenid@ $name"
            tvWelcome?.visibility = View.VISIBLE
        }

        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            SongFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }

            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSongs)

        // Notifica a la actividad que el RecyclerView está listo
        listener?.onRecyclerViewReady(recyclerView)
    }

    interface FragmentListener {
        fun onRecyclerViewReady(recyclerView: RecyclerView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar FragmentListener")
        }
    }

    }