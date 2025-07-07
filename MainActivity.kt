package com.tuusuario.cursorassistant

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var ballView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ballView = findViewById(R.id.ballView)

        // Simula emociones cambiando color cada 5 segundos
        ballView.setOnClickListener {
            changeEmotion()
        }

        changeEmotion()
    }

    private fun changeEmotion() {
        val colors = listOf(
            Color.YELLOW,  // Feliz
            Color.BLUE,    // Triste
            Color.RED,     // Enojada
            Color.GRAY,    // Aburrida
            Color.MAGENTA  // Nerviosa/Enamorada
        )
        val color = colors[Random.nextInt(colors.size)]
        ballView.setColorFilter(color)
    }
}
