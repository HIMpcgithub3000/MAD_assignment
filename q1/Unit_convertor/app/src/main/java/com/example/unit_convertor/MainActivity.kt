package com.example.unit_convertor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.ui.semantics.text
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var convertButton: AppCompatButton
    private var fromUnit: String = "Feet"
    private var toUnit: String = "Inches"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputEditText = findViewById(R.id.inputEditText)
        resultTextView = findViewById(R.id.resultTextView)
        fromSpinner = findViewById(R.id.fromSpinner)
        toSpinner = findViewById(R.id.toSpinner)
        convertButton = findViewById(R.id.convertButton)
        val units = arrayOf("Feet", "Inches", "Centimeters", "Meters", "Yards")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                fromUnit = units[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                toUnit = units[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        convertButton.setOnClickListener {
            convert()
        }
    }

    private fun convert() {
        val value = inputEditText.text.toString().toDoubleOrNull()
        if (value == null) {
            resultTextView.text = "Invalid Input"
            return
        }
        val meters = when (fromUnit) {
            "Feet" -> value * 0.3048
            "Inches" -> value * 0.0254
            "Centimeters" -> value / 100.0
            "Meters" -> value
            "Yards" -> value * 0.9144
            else -> 0.0
        }
        val result = when (toUnit) {
            "Feet" -> meters / 0.3048
            "Inches" -> meters / 0.0254
            "Centimeters" -> meters * 100.0
            "Meters" -> meters
            "Yards" -> meters / 0.9144
            else -> 0.0
        }
        val decimalFormat = DecimalFormat("#.####")
        resultTextView.text = decimalFormat.format(result).toString()
    }
}