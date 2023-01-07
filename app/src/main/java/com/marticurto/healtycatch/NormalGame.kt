package com.marticurto.healtycatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import com.marticurto.healtycatch.clases.Game
import java.util.*
import kotlin.properties.Delegates

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

        //a partir del observador podemos pintar la cesta donde queremos (en el centro) y las frutas abajo
        obs.addOnGlobalLayoutListener {
            game!!.ancho = game!!.width
            game!!.alto = game!!.height
            game!!.cestaX = game!!.ancho / 2
            game!!.cestaY = 250
            game!!.posAppleY=game!!.alto
            game!!.posBananaY=game!!.alto
            game!!.radio = 68
        }

        //Ejecutamos cada 20 milisegundos
        val appleTimer = Timer()
        appleTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { //Cada 0.02 segundos movemos las diferentes piezas y repintamos
                    //si ganamos o perdemos al cabo de 3s volvemos a la pantalla inicial
                    if(game!!.puntuacion>=30|| game!!.puntuacion<0){
                        Handler().postDelayed({
                            this@NormalGame.finish()
                        }, 3000)
                    }else{
                        //mientras no ganemos, movemos la piezas
                        game!!.posAppleY -= 10
                        game!!.posBananaY -= 15
                        game!!.posBurgerY -= 5
                        //refreca la pantalla y llama al draw
                        game!!.invalidate()
                    }
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