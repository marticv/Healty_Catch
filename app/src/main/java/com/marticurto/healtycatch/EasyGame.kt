package com.marticurto.healtycatch

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.marticurto.healtycatch.clases.Game


class EasyGame : AppCompatActivity() {

    var game: Game? = null
    private val appleHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy_game)

        //creamos las variables necesasias y definimos que no haya enemigos
        game = findViewById<View>(R.id.Pantalla) as Game
        val obs = game!!.viewTreeObserver
        game!!.enemyActive=false

        //creamos un listener para saber las medidas de la pantalla una vez pintada y asi poder colocar la cesta
        obs.addOnGlobalLayoutListener {
            game!!.ancho = game!!.width
            game!!.alto = game!!.height
            game!!.cestaX = game!!.ancho / 2
            game!!.cestaY = 250
            game!!.radio = 50
        }

        //Ejecutamos cada 20 milisegundos
        val appleTimer = Timer()
        appleTimer.schedule(object : TimerTask() {
            override fun run() {
                appleHandler.post { //Cada 20 segundos movemos las frutas
                    game!!.posAppleY -= 10
                    game!!.posBananaY -= 12
                    //refreca la pantalla y llama al draw
                    game!!.invalidate()
                }
            }
        }, 0, 20)
    }

    //cuando se destruye la actividad paramos la musica y liberamos recursos
    override fun onDestroy() {
        super.onDestroy()
        game?.gameLoop?.stop()
        game?.gameLoop?.release()
    }
}