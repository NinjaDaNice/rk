package com.example.midterm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity__main.*

class SingeActivity : AppCompatActivity() {

    private var resultLine = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity__main)
        setNumbers()
        setActions()
        setOperations()
    }

    private fun changeResultWithAdd(symbol: Char) {
        resultLine += symbol
        resultTextView.text = resultLine
    }

    private fun canAddDot(): Boolean {
        var i = resultLine.length - 1
        var hasDigit = false
        while (i >= 0) {
            if (resultLine[i] in '0'..'9') {
                hasDigit = true
            }
            if ('.' == resultLine[i]) {
                return false
            }
            if ("÷-+×%".contains(resultLine[i]) && hasDigit) {
                return true
            }
            i--
        }
        return resultLine.isNotEmpty()
    }

    private fun setNumbers() {
        val numbers = arrayListOf(
            Pair(zeroTextView, "0"),
            Pair(oneTextView, "1"),
            Pair(twoTextView, "2"),
            Pair(threeTextView, "3"),
            Pair(fourTextView, "4"),
            Pair(fiveTextView, "5"),
            Pair(sixTextView, "6"),
            Pair(sevenTextView, "7"),
            Pair(eightTextView, "8"),
            Pair(nineTextView, "9")
        )
        dotTextView.setOnClickListener {
            if (canAddDot()) {
                changeResultWithAdd('.')
            }
        }
        for (number in numbers) {
            number.first.setOnClickListener {
               resultLine += number.second
                resultTextView.text = resultLine
            }
        }
    }

    private fun setActions() {
        clearTextView.setOnClickListener {
            resultLine = ""
            resultTextView.text = resultLine
        }
    }

    private fun normalize() {
        if (resultLine.isNotEmpty() && "÷.-+×%".contains(resultLine.last())) {
            resultLine = resultLine.substring(0, resultLine.length - 1)
        }
    }

    private fun setOperations() {
        plusTextView.setOnClickListener {
            normalize()
            changeResultWithAdd('+')
        }
        minusTextView.setOnClickListener {
            normalize()
            changeResultWithAdd('-')
        }
        divideTextView.setOnClickListener {
            normalize()
            changeResultWithAdd('÷')
        }
        multiTextView.setOnClickListener {
            normalize()
            changeResultWithAdd('×')
        }
        percentTextView.setOnClickListener {
            normalize()
            changeResultWithAdd('%')
        }
        equalsTextView.setOnClickListener {
            normalize()
            calculate()
        }
    }

    private fun calculate() {
        var number = 0f
        var startIndex = -1
        var operation = '.'
        for (i in resultLine.indices) {
            if ("÷-+×%".contains(resultLine[i])) {
                number = resultLine.substring(0, i).toFloat()
                startIndex = i + 1
                operation = resultLine[i]
                break
            }
        }
        if (startIndex == -1) {
            return
        }
        for (i in startIndex until resultLine.length) {
            if ("÷-+×%".contains(resultLine[i])) {
                val secondNumber = resultLine.substring(startIndex, i).toFloat()
                startIndex = i + 1
                number = doOperation(secondNumber, number, operation)
                operation = resultLine[i]
            }
        }
        if (startIndex != -1) {
            number = doOperation(resultLine.substring(startIndex).toFloat(), number, operation)
            resultLine = number.toString()
            resultTextView.text = resultLine
        }
    }

    private fun doOperation(secondNumber: Float, firstNumber: Float, operation: Char): Float =
        when (operation) {
            '÷' -> firstNumber / secondNumber
            '-' -> firstNumber - secondNumber
            '+' -> firstNumber + secondNumber
            '×' -> firstNumber * secondNumber
            else -> (firstNumber / 100) * secondNumber
        }
}