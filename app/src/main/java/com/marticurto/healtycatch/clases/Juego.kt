package com.marticurto.healtycatch.clases

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.marticurto.healtycatch.R
import java.util.*

/**
 * Clase que representa los objetos del juego y sus interacciones
 *
 * imagenes de https://www.cleanpng.com/
 *
 */
// Extensión de una View. Totalmente necesario para dibujar
class Juego : View {
    var ancho = 0
    var alto = 0
    var escala = 0f
    var appleX = 0
    var appleY = 0
    var bananaX = 0
    var bananaY =0
    var radio = 0
    var posAppleX = 0
    var posAppleY = 0
    var posBananaX = 0
    var posBananaY = 0
    var posBurgerX = 0
    var posBurgerY = 0

    private val gestos: GestureDetector? = null
    private var rectCesta: RectF? = null
    private var rectApple: RectF? = null
    private var rectBanana: RectF?=null
    private var rectBurger: RectF?=null
    private val random = Random()
    private var puntuacion:Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    //Sección que capta los eventos del usuario
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // solo nos interesa captar el movimiento lateral y actuar en ese momento
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_MOVE -> {
                appleX = event.x.toInt()
                radio = 50
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

        //Definimos los colores de los objetos a pintar
        fondo.color = Color.WHITE
        fondo.style = Paint.Style.FILL_AND_STROKE
        basket.color = Color.YELLOW
        basket.style = Paint.Style.FILL_AND_STROKE
        apple.color = Color.RED
        apple.style = Paint.Style.FILL_AND_STROKE
        puntos.setTextAlign(Paint.Align.RIGHT);
        puntos.setTextSize(100F);
        puntos.setColor(Color.BLUE);
        //puntos.setTypeface(typeface);

        //Pinto rectángulo con un ancho y alto de 1000 o de menos si la pantalla es menor.
        canvas.drawRect(Rect(0, 0, ancho, alto), fondo)

        //creamos bitmap con la cesta y alimentos
        var imgBasket: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.basket_upsidedown)
        var imgApple: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.apple)
        var imgBanana: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.banana)
        var imgBurger: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.burger)

        // Pinto la cesta.
        rectCesta = RectF(
            (appleX - radio).toFloat(),
            (appleY - radio).toFloat(),
            (appleX + radio).toFloat(),
            (appleY + radio).toFloat()
        )

        //si la manzana o el platano llegan arriba, aparece uno nuevo
        if (posAppleY < 0) {
            posAppleY = alto-50
            posAppleX = random.nextInt(ancho)
        }

        if (posBananaY < 0) {
            posBananaY = alto-50
            posBananaX = random.nextInt(ancho)
        }

        if (posBurgerY < 0) {
            posBurgerY  = alto-50
            posBurgerX = random.nextInt(ancho)
        }

        //pintamos recuadro para la fruta
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

        rectBurger = RectF(
            (posBurgerX - radio).toFloat(),
            (posBurgerY - radio).toFloat(),
            (posBurgerX + radio).toFloat(),
            (posBurgerY + radio).toFloat()
        )

        //añado la cesta y las frutas al view al view
        canvas.drawBitmap(imgBasket,(appleX - radio).toFloat(),(appleY - radio).toFloat(),basket)
        canvas.drawBitmap(imgApple,(posAppleX - radio).toFloat(),(posAppleY - radio).toFloat(),apple)
        canvas.drawBitmap(imgBanana,(posBananaX - radio).toFloat(),(posBananaY - radio).toFloat(),banana)
        canvas.drawBitmap(imgBurger,(posBurgerX - radio).toFloat(),(posBurgerY - radio).toFloat(),burger)

        //canvas.drawOval(rectCesta!!, cesta)

        //modificamos la puntuacion si se recoge una fruta
        if (RectF.intersects(rectCesta!!, rectApple!!)) {
            puntuacion += 2;
            posAppleY=alto-50;
            posAppleX= random.nextInt(ancho);
        }

        if(RectF.intersects(rectCesta!!,rectBanana!!)){
            puntuacion +=5
            posBananaY=alto-50
            posBananaX=random.nextInt(ancho)
        }

        if(RectF.intersects(rectCesta!!,rectBurger!!)){
            puntuacion -=1
            posBurgerY=alto-50
            posBurgerX=random.nextInt(ancho)
        }
        canvas.drawText(puntuacion.toString(), 150F, 150F,puntos);
    }
}