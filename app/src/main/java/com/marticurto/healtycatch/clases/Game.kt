package com.marticurto.healtycatch.clases

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.marticurto.healtycatch.R
import java.util.*
import kotlin.random.Random.*

/**
 * Clase que representa los objetos del juego y sus interacciones
 *
 * imagenes de https://www.cleanpng.com/
 * musica de https://file-examples.com
 * efectos de Sound Effect from https://pixabay.com
 *
 */
// Extendemos de view para dibujar
open class Game//en el constructor hacemos que empieze a sonar la musica en loop
    (context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //creacion de variables necesarias
    var ancho = 0
    var alto = 0
    var cestaX = 0
    var cestaY = 0
    var radio = 0
    private var posAppleX = 0
    var posAppleY = 0
    private var posBananaX = 0
    var posBananaY = 0
    private var posBurgerX = 0
    var posBurgerY = 0

    //variable para ver si ponemos enemigos
    var enemyActive = false

    //variables para el sonido y musica
    var gameLoop: MediaPlayer = MediaPlayer.create(context,R.raw.music)
    private var error: MediaPlayer = MediaPlayer.create(context,R.raw.error)
    private var beep: MediaPlayer = MediaPlayer.create(context,R.raw.beep)

    private var rectCesta: RectF? = null
    private var rectApple: RectF? = null
    private var rectBanana: RectF?=null
    private var rectBurger: RectF?=null
    private val random = Random()
    var puntuacion:Int = 0

    //en el constructor definimos el comportamiento de la musica
    init {
        gameLoop = MediaPlayer.create(context,R.raw.music)
        gameLoop.start()
        gameLoop.setOnCompletionListener { gameLoop.start() }
    }

    //Sección que capta los eventos del usuario
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // solo nos interesa captar el movimiento lateral y actuar en ese momento
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_MOVE -> {
                cestaX = event.x.toInt()
                radio = 68
                //repintamos la cesta al moverla
                this.invalidate()
            }
        }
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Definimos los objetos a pintar
        val fondo = Paint()
        val basket = Paint()
        val apple = Paint()
        val banana = Paint()
        val burger = Paint()
        val puntos = Paint()
        val texto = Paint()

        //Definimos los colores de los objetos a pintar
        fondo.color = Color.BLUE
        fondo.style = Paint.Style.FILL_AND_STROKE
        puntos.textAlign = Paint.Align.RIGHT
        puntos.textSize = 100F
        puntos.color = Color.WHITE
        texto.color=Color.BLACK
        texto.textAlign = Paint.Align.CENTER
        texto.textSize= 100F

        //Pinto rectángulo con un ancho y alto de 1000 o de menos si la pantalla es menor.
        canvas.drawRect(Rect(0, 0, ancho, alto), fondo)

        //creamos bitmap con la cesta y alimentos
        val imgBasket: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.basket)
        val imgApple: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.apple)
        val imgBanana: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.banana)
        val imgBurger: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.burger)

        // Pinto la cesta.
        rectCesta = RectF(
            (cestaX - radio).toFloat(),
            (cestaY - radio).toFloat(),
            (cestaX + radio).toFloat(),
            (cestaY + radio).toFloat()
        )

        //si la manzana o el platano llegan arriba, aparece uno nuevo
        if (posAppleY < 0) {
            posAppleY = alto-50
            posAppleX = random.nextInt(ancho)
            puntuacion -=1
        }
        if (posBananaY < 0) {
            posBananaY = alto-50
            posBananaX = random.nextInt(ancho)
            puntuacion -=1
        }

        //pintamos recuadro para las frutas
        rectApple = RectF(
            (posAppleX - radio).toFloat(),
            (posAppleY - radio).toFloat(),
            (posAppleX + radio).toFloat(),
            (posAppleY + radio).toFloat()
        )
        rectBanana = RectF(
            (posBananaX - radio).toFloat(),
            (posBananaY - radio).toFloat(),
            (posBananaX + radio).toFloat(),
            (posBananaY + radio).toFloat()
        )

        //añado la cesta y las frutas al view al view
        canvas.drawBitmap(imgBasket,null, rectCesta!!,basket)
        canvas.drawBitmap(imgApple,null, rectApple!!,apple)
        canvas.drawBitmap(imgBanana,null,rectBanana!!,banana)

        //modificamos la puntuacion si se recoge una fruta
        if (RectF.intersects(rectCesta!!, rectApple!!)) {
            puntuacion += 2
            posAppleY=alto-50
            posAppleX= random.nextInt(ancho)
            if(beep.isPlaying){
                beep.stop()
                beep.prepare()
                beep.start()
            }else{
                beep.start()
            }
        }

        if(RectF.intersects(rectCesta!!,rectBanana!!)){
            puntuacion +=5
            posBananaY=alto-50
            posBananaX=random.nextInt(ancho)
            if(beep.isPlaying){
                beep.stop()
                beep.prepare()
                beep.start()
            }else{
                beep.start()
            }
        }
        canvas.drawText(puntuacion.toString(), 150F, 150F,puntos)

        if(puntuacion<=-1){
            texto.color=Color.RED
            posAppleY=0
            posBananaY=0
            posBurgerY=0
            canvas.drawRect(Rect(0, 0, ancho, alto), fondo)
            canvas.drawText("Has muerto",300F,400F,texto)
        }

        if(puntuacion>=30){
            texto.color=Color.BLACK
            posAppleY=0
            posBananaY=0
            posBurgerY=0
            canvas.drawRect(Rect(0, 0, ancho, alto), fondo)
            canvas.drawText("Has ganado!",300F,400F,texto)
        }

        //en caso de que queramos enemigos tambien los pintamos
        //y definimos su comportamiento
        if (enemyActive) {
            if (posBurgerY < 0) {
                posBurgerY = alto - 50
                posBurgerX = random.nextInt(ancho)
            }
            rectBurger = RectF(
                (posBurgerX - radio).toFloat(),
                (posBurgerY - radio).toFloat(),
                (posBurgerX + radio).toFloat(),
                (posBurgerY + radio).toFloat()
            )
            canvas.drawBitmap(imgBurger,null, rectBurger!!,burger)

            if(RectF.intersects(rectCesta!!,rectBurger!!)){
                puntuacion -=1
                posBurgerY=alto-50
                posBurgerX=random.nextInt(ancho)
                error.start()
            }

            if(puntuacion<=-1){
                texto.color=Color.RED
                posAppleY=0
                posBananaY=0
                posBurgerY=0
                canvas.drawRect(Rect(0, 0, ancho, alto), fondo)
                canvas.drawText("Has muerto",300F,400F,texto)
            }

            if(puntuacion>=30){
                texto.color=Color.BLACK
                posAppleY=0
                posBananaY=0
                posBurgerY=0
                canvas.drawRect(Rect(0, 0, ancho, alto), fondo)
                canvas.drawText("Has ganado!",300F,400F,texto)
            }
        }
    }

}