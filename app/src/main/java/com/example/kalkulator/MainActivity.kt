package com.example.kalkulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kalkulator.databinding.ActivityMainBinding
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListener for all buttons
        binding.apply {
            tombol0.setOnClickListener { appendNumber("0") }
            tombol1.setOnClickListener { appendNumber("1") }
            tombol2.setOnClickListener { appendNumber("2") }
            tombol3.setOnClickListener { appendNumber("3") }
            tombol4.setOnClickListener { appendNumber("4") }
            tombol5.setOnClickListener { appendNumber("5") }
            tombol6.setOnClickListener { appendNumber("6") }
            tombol7.setOnClickListener { appendNumber("7") }
            tombol8.setOnClickListener { appendNumber("8") }
            tombol9.setOnClickListener { appendNumber("9") }

            tombolTambah.setOnClickListener { appendOperator("+") }
            tombolKurang.setOnClickListener { appendOperator("-") }
            tombolKali.setOnClickListener { appendOperator("*") }
            tombolBagi.setOnClickListener { appendOperator("/") }

            tombolSamaDengan.setOnClickListener { calculateResult() }
            tombolC.setOnClickListener { clearInput() }
        }
    }

    private fun appendNumber(number: String) {
        input += number
        binding.inputTextView.text = input
    }

    private fun appendOperator(op: String) {
        if (input.isNotEmpty() && !isOperator(input.last())) {
            input += " $op "
            binding.inputTextView.text = input
        }
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(input)
            binding.resultTextView.text = result.toString()
        } catch (e: Exception) {
            binding.resultTextView.text = "Error"
        }
    }

    private fun clearInput() {
        input = ""
        binding.inputTextView.text = ""
        binding.resultTextView.text = ""
    }

    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '*' || c == '/'
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ").filter { it.isNotEmpty() }
        val values = Stack<Double>()
        val operators = Stack<Char>()

        val precedence = mapOf(
            '+' to 1,
            '-' to 1,
            '*' to 2,
            '/' to 2
        )

        fun applyOperator(op: Char) {
            val right = values.pop()
            val left = values.pop()
            val result = when (op) {
                '+' -> left + right
                '-' -> left - right
                '*' -> left * right
                '/' -> left / right
                else -> throw IllegalArgumentException("Invalid operator")
            }
            values.push(result)
        }

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> values.push(token.toDouble())
                token.length == 1 && token[0] in precedence.keys -> {
                    while (operators.isNotEmpty() && precedence[operators.peek()]!! >= precedence[token[0]]!!) {
                        applyOperator(operators.pop())
                    }
                    operators.push(token[0])
                }
            }
        }

        while (operators.isNotEmpty()) {
            applyOperator(operators.pop())
        }

        return values.pop()
    }
}
