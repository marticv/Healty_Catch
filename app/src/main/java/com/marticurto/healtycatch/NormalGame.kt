package com.marticurto.healtycatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.marticurto.healtycatch.clases.Game
import java.util.*

class NormalGame : AppCompatActivity() {

    var game: Game? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_game)

        //creamos una variable para controlar el juego y un observador para poder tener
        //las medidas correctas.
        //declaramos que hay enemigos activos
        game = findViewById<View>(R.id.vwPantallaNormal) as Game
        val obs = game!!.viewTreeObserver
        game!!.enemyActive=true

        //a partir del observador podemos pintar la cesta donde queremos (en el centro)
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
                handler.post { //Cada 0.02 segundos movemos las diferentes piezas y repintamos
                    game!!.posAppleY -= 10
                    game!!.posBananaY -= 12
                    game!!.posBurgerY -= 5
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