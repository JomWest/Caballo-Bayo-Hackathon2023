package com.yonjar.newjambox

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.yonjar.newjambox.Classes.Cancion
import com.yonjar.newjambox.Classes.CancionesAdapter

class MainActivity : AppCompatActivity(), SongFragment.FragmentListener {

    lateinit var navegation: BottomNavigationView

    private val onNavMenu = BottomNavigationView.OnNavigationItemSelectedListener {
            item ->
        when(item.itemId){
            R.id.Home ->{
                supportFragmentManager.commit {
                    replace<HomeFragment>(R.id.FragmentInterface)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.ExplorarMusica ->{
                supportFragmentManager.commit {
                    replace<SongFragment>(R.id.FragmentInterface)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.Usuario ->{
                supportFragmentManager.commit {
                    replace<ProfileFragment>(R.id.FragmentInterface)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.Artistas ->{
                supportFragmentManager.commit {
                    replace<ArtistFragment>(R.id.FragmentInterface)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    val listaCanciones = listOf<Cancion>(
        Cancion("Badblood","Tarloyr Swift","badblood.mp3",R.drawable.taylor),
        Cancion("Diamond","Rihanna","diamond.mp3",R.drawable.rihanna),
        Cancion("Without me","Eminem","withoutme.mp3",R.drawable.eminem),
        Cancion("Voices","Rev Theory","randy.mp3",R.drawable.randy),
        Cancion("Numb","Linkin Park","numb.mp3",R.drawable.numb)
    )
    val fd by lazy{
        assets.openFd(listaCanciones[cancionActualIndex].AudioCancion)
    }

    val mp by lazy{
        val m = MediaPlayer()

        m.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        fd.close()
        m.prepare()
        m
    }

    val controles by lazy {
        listOf(R.id.imPausa,R.id.imPlay,R.id.imSiguiente,R.id.imAnterior).map {
            findViewById<ImageView>(it)
        }
    }

    object ci{
        val pausa = 0
        val play = 1
        val siguiente =2
        val anterior = 3

    }

    val tvNombreCancion by lazy{
        findViewById<TextView>(R.id.tvMusicName)
    }
    val imagenCancion by lazy{
        findViewById<ImageView>(R.id.ivSong)
    }


    var cancionActualIndex = 0
        set(value) {
            var v = if(value==-1){
                listaCanciones.size-1
            }
            else{
                value%listaCanciones.size
            }
            field = v
            nombreCancionActual = listaCanciones[v].AudioCancion
        }
    lateinit var nombreCancionActual:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controles[ci.play].setOnClickListener(this::play)
        controles[ci.pausa].setOnClickListener(this::pausar)
        controles[ci.siguiente].setOnClickListener(this::siguienteCancion)
        controles[ci.anterior].setOnClickListener (this::cancionAnterior)

        nombreCancionActual = listaCanciones[cancionActualIndex].nombreCancion
        tvNombreCancion.text = nombreCancionActual
        tvNombreCancion.isSelected = true
        imagenCancion.setImageResource(listaCanciones[cancionActualIndex].imagenCancion)

        navegation = findViewById(R.id.Navegacion)
        navegation.setOnNavigationItemSelectedListener(onNavMenu)

        supportFragmentManager.commit {
            replace<HomeFragment>(R.id.FragmentInterface)
            setReorderingAllowed(true)
            addToBackStack("replacement")

        }
    }

    override fun onRecyclerViewReady(recyclerView: RecyclerView) {
        recyclerView.adapter = CancionesAdapter(listaCanciones, this)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL, false)
        findViewById<ConstraintLayout>(R.id.constraintLayout).visibility = View.VISIBLE
    }




    private fun play(view: View){

        if(!mp.isPlaying){
            mp.start()
            tvNombreCancion.visibility = View.VISIBLE
            imagenCancion.visibility = View.VISIBLE
            controles[ci.play].setImageResource(R.drawable.pausa)

        }
        else{
            mp.pause()
            controles[ci.play].setImageResource(R.drawable.play)

        }
    }

    private fun pausar(view: View){
        if(mp.isPlaying){
            mp.pause()
            tvNombreCancion.visibility = View.GONE
            imagenCancion.visibility = View.GONE
            controles[ci.play].setImageResource(R.drawable.play)
        }
        mp.seekTo(0)
    }

    fun resetCancion(){
        mp.reset()
        val fd = assets.openFd(nombreCancionActual)
        mp.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        mp.prepare()
        play(controles[ci.play])

        tvNombreCancion.text = listaCanciones[cancionActualIndex].nombreCancion
        imagenCancion.setImageResource(listaCanciones[cancionActualIndex].imagenCancion)
    }

    private fun siguienteCancion(view: View){
        cancionActualIndex++
        resetCancion()
    }

    private fun cancionAnterior(view: View){
        cancionActualIndex--
        resetCancion()
    }
}