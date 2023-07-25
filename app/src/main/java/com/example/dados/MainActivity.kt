package com.example.dados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var buttonPressCount = 0
    private val buttonMaxCount = 5
    private val numeroslist = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boton: Button = findViewById(R.id.boton)
        val reset: Button = findViewById(R.id.reset)
        val dado: ImageView = findViewById(R.id.imageView)
        val mensaje: TextView = findViewById(R.id.sub)
        val vec: TextView = findViewById(R.id.textView)
        var ints = 5
        var winner = false
        var exit: TextView = findViewById(R.id.x)

        exit.setOnClickListener{
            finish()
        }

        reset.setOnClickListener {
            if (buttonPressCount == buttonMaxCount) {
                buttonPressCount = 0
                boton.isEnabled = true
                boton.text = "Tirar"
                numeroslist.clear()
                dado.setImageResource(R.drawable.dice_1)
                vec.text = "Intentos: 5"
                ints = 5
                if (winner == true){
                    mensaje.text = "Bien hecho! Otra vez?"
                } else{
                    mensaje.text = "Tu puedes!"
                }
            } else{
                if (ints < 5) {
                    reset.text = "Termina tu jugada"
                }
            }
        }

        boton.setOnClickListener {
            if (buttonPressCount < buttonMaxCount) {
                val numero = rand(1, 6)
                when (numero) {
                    1 -> dado.setImageResource(R.drawable.dice_1)
                    2 -> dado.setImageResource(R.drawable.dice_2)
                    3 -> dado.setImageResource(R.drawable.dice_3)
                    4 -> dado.setImageResource(R.drawable.dice_4)
                    5 -> dado.setImageResource(R.drawable.dice_5)
                    6 -> dado.setImageResource(R.drawable.dice_6)
                }

                ints -= 1
                val texto = "Intentos: ${ints.toString()}"
                vec.text = texto

                val men = "$numero"
                mensaje.text = men

                val bot = "Tirar otra vez"
                boton.text = bot

                numeroslist.add(numero)
                buttonPressCount ++


            }

            if (buttonPressCount == buttonMaxCount){
                boton.isEnabled = false
                val texto = numeroslist.joinToString(", ")
                vec.text = texto
                reset.text = "Reiniciar"

                val first = numeroslist.first()
                var c = 0
                for (num in numeroslist){
                    if (num == first){
                        c += 1
                    }
                }

                if (c == numeroslist.size){
                    mensaje.text = "Generala!"
                    boton.text = "Ganaste!"
                    winner = true
                } else if (hasPoker()) {
                    mensaje.text = "Póker!"
                    boton.text = "Ganaste!"
                    winner = true
                } else if (hasFull()) {
                    mensaje.text = "Full!"
                    boton.text = "Ganaste!"
                    winner = true
                } else if (hasEscalera()){
                    mensaje.text = "Escalera!"
                    boton.text = "Ganaste!"
                    winner = true
                } else{
                    mensaje.text = "Nada... Otra vez?"
                    boton.text = "Perdiste!"
                    boton.isEnabled = false
                    winner = false
                }
            }
        }
    }

    fun rand(from: Int, to: Int): Int {
        require(from <= to) {"Invalid range"}
        return Random.nextInt(from, to + 1)
    }

    fun hasPoker(): Boolean{
        val ocurrencesMap = numeroslist.groupingBy { it }.eachCount()
        return ocurrencesMap.any{it.value >= 4}
    }

    fun hasFull(): Boolean {
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

    fun hasEscalera(): Boolean {
        val sequence = numeroslist.joinToString("")
        val validSequences = setOf("123456", "234561", "345612", "456123", "561234", "612345")
        return validSequences.contains(sequence)
    }
}
