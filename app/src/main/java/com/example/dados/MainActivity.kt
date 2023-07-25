package com.example.dados

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var buttonPressCount = 0
    private val buttonMaxCount = 5
    private val numeroslist = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.parseColor("#DF7401")

        val boton: Button = findViewById(R.id.boton)
        val reset: Button = findViewById(R.id.reset)
        val dado: ImageView = findViewById(R.id.imageView)
        val mensaje: TextView = findViewById(R.id.sub)
        val vec: TextView = findViewById(R.id.textView)
        var ints = 5
        var winner = false

        reset.setOnClickListener {
            if (buttonPressCount == buttonMaxCount) {
                buttonPressCount = 0
                boton.isEnabled = true
                boton.text = getString(R.string.boton_tirar)
                numeroslist.clear()
                dado.setImageResource(R.drawable.dice_1)
                vec.text = getString(R.string.intentos)
                ints = 5
                if (winner){
                    mensaje.text = getString(R.string.algo)
                } else{
                    mensaje.text = getString(R.string.aliento)
                }
            } else{
                if (ints < 5) {
                    reset.text = getString(R.string.boton_reiniciar2)
                }
            }
        }

        boton.setOnClickListener {
            if (buttonPressCount < buttonMaxCount) {
                val numero = rand()
                when (numero) {
                    1 -> dado.setImageResource(R.drawable.dice_1)
                    2 -> dado.setImageResource(R.drawable.dice_2)
                    3 -> dado.setImageResource(R.drawable.dice_3)
                    4 -> dado.setImageResource(R.drawable.dice_4)
                    5 -> dado.setImageResource(R.drawable.dice_5)
                    6 -> dado.setImageResource(R.drawable.dice_6)
                }

                ints -= 1
                val texto = "Intentos: $ints"
                vec.text = texto

                val men = "$numero"
                mensaje.text = men

                val bot = getString(R.string.boton_tirar2)
                boton.text = bot

                numeroslist.add(numero)
                buttonPressCount ++

            }

            if (buttonPressCount == buttonMaxCount){
                boton.isEnabled = false
                val texto = numeroslist.joinToString(", ")
                vec.text = texto
                reset.text = getString(R.string.boton_reiniciar)

                val first = numeroslist.first()
                var c = 0
                for (num in numeroslist){
                    if (num == first){
                        c += 1
                    }
                }

                if (c == numeroslist.size){
                    mensaje.text = getString(R.string.generala)
                    boton.text = getString(R.string.ganaste)
                    winner = true
                } else if (hasPoker()) {
                    mensaje.text = getString(R.string.poker)
                    boton.text = getString(R.string.ganaste)
                    winner = true
                } else if (hasFull()) {
                    mensaje.text = getString(R.string.full)
                    boton.text = getString(R.string.ganaste)
                    winner = true
                } else if (hasEscalera()){
                    mensaje.text = getString(R.string.escalera)
                    boton.text = getString(R.string.ganaste)
                    winner = true
                } else{
                    mensaje.text = getString(R.string.nada)
                    boton.text = getString(R.string.perdiste)
                    boton.isEnabled = false
                    winner = false
                }
            }
        }
    }

    private fun rand(): Int {
        return Random.nextInt(1, 7)
    }

    private fun hasPoker(): Boolean{
        val ocurrencesMap = numeroslist.groupingBy { it }.eachCount()
        return ocurrencesMap.any{it.value >= 4}
    }

    private fun hasFull(): Boolean {
        val occurrencesMap = numeroslist.groupingBy { it }.eachCount()

        val hasThreeOfAKind = occurrencesMap.containsValue(3)
        val hasPair = occurrencesMap.containsValue(2)

        if (hasThreeOfAKind && hasPair) {
            val valuesWithThreeOfAKind = occurrencesMap.filter { it.value == 3 }.keys.toList()
            val valuesWithPair = occurrencesMap.filter { it.value == 2 }.keys.toList()

            return valuesWithThreeOfAKind.size == 1 && valuesWithPair.size == 1
        }

        return false
    }

    private fun hasEscalera(): Boolean {
        val sequence = numeroslist.joinToString("")
        val validSequences = setOf("123456", "234561", "345612", "456123", "561234", "612345")
        return validSequences.contains(sequence)
    }
}
