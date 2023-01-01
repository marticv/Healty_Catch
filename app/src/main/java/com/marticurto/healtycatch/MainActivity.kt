package com.marticurto.healtycatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btEasy:Button
    lateinit var btNormal:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inciamos variables y les damos funcionalidad
        btEasy = findViewById(R.id.btEasy)
        btNormal = findViewById(R.id.btNormal)

        btEasy.setOnClickListener { initiateEasyGame() }
        btNormal.setOnClickListener { initiateNormalGame() }
    }

    private fun initiateEasyGame(){
        val intent= Intent(this, EasyGame::class.java)
        startActivity(intent)
    }

    private fun initiateNormalGame(){
        val intent= Intent(this, NormalGame::class.java)
        startActivity(intent)
    }
}