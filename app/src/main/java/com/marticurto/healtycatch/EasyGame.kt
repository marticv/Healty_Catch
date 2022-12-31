package com.marticurto.healtycatch

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.marticurto.healtycatch.clases.Juego


class EasyGame : AppCompatActivity() {

    var juego: Juego? = null
    private val appleHandler = Handler()
    private val bananaHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy_game)

        juego = findViewById<View>(R.id.Pantalla) as Juego
        val obs = juego!!.viewTreeObserver
        obs.addOnGlobalLayoutListener { // Sólo se puede averiguar el ancho y alto una vez ya se ha pintado el layout. Por eso se calcula en este listener
            juego!!.ancho = juego!!.width
            juego!!.alto = juego!!.height
            juego!!.appleX = juego!!.ancho / 2
            juego!!.bananaX = juego!!.ancho / 2
            juego!!.appleY = 250
            juego!!.bananaY = 250
            juego!!.radio = 50
            juego!!.posAppleY = juego!!.alto-50
        }

        //Ejecutamos cada 20 milisegundos
        val appleTimer = Timer()
        appleTimer.schedule(object : TimerTask() {
            override fun run() {
                appleHandler.post { //Cada 20 segundos movemos la moneda 10dp
                    juego!!.posAppleY -= 10
                    //refreca la pantalla y llama al draw
                    juego!!.invalidate()
                }
            }
        }, 0, 20)

        val bananaTimer = Timer()
        bananaTimer.schedule(object : TimerTask() {
            override fun run() {
                bananaHandler.post { //Cada 20 segundos movemos la moneda 10dp
                    juego!!.posBananaY -= 10
                    //refreca la pantalla y llama al draw
                    juego!!.invalidate()
                }
            }
        },1000, 20)
    }
}