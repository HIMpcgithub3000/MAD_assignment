package com.example.unit_convertor;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class UnitConverterActivity extends AppCompatActivity {

    private EditText inputEditText;
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private TextView resultTextView;
    private Button convertButton;

    private String fromUnit;
    private String toUnit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = findViewById(R.id.inputEditText);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        resultTextView = findViewById(R.id.resultTextView);
        convertButton = findViewById(R.id.convertButton);

        // Populate Spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        fromUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });

        toUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });

        convertButton.setOnClickListener(v -> {
            String input = inputEditText.getText().toString();
            if (input.isEmpty()) {
                Toast.makeText(UnitConverterActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            double inputValue = Double.parseDouble(input);
            double result = convert(inputValue, fromUnit, toUnit);
            resultTextView.setText(String.valueOf(result));
        });
    }

    private double convert(double value, String from, String to) {
        // Convert everything to meters first
        double inMeters;
        switch (from) {
            case "Feet":
                inMeters = value * 0.3048;
                break;
            case "Inches":
                inMeters = value * 0.0254;
                break;
            case "Centimeters":
                inMeters = value * 0.01;
                break;
            case "Yards":
                inMeters = value * 0.9144;
                break;
            case "Meters":
            default:
                inMeters = value;
                break;
        }

        // Then, convert from meters to the desired unit
        switch (to) {
            case "Feet":
                return inMeters / 0.3048;
            case "Inches":
                return inMeters / 0.0254;
            case "Centimeters":
                return inMeters / 0.01;
            case "Yards":
                return inMeters / 0.9144;
            case "Meters":
            default:
                return inMeters;
        }
    }
}