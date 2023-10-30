package com.example.mipt_lab3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
public class MainActivity extends AppCompatActivity {
    private TextView Screen;
    private StringBuilder currentInput = new StringBuilder();
    private double result = 0;
    private char currentOperator = ' ';
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");
    boolean isCalculatedValue = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Screen = findViewById(R.id.tvScreen);
    }
    public void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        if (buttonText.matches("[0-9.]")) {
            handleNumberInput(buttonText);
        } else if (buttonText.matches("[+\\-*/]")) {
            handleOperatorInput(buttonText);
        } else if (buttonText.equals("=")) {
            calculateResult();
        } else if (buttonText.equals("CE")) {
            clearAll();
        } else if (buttonText.equals("←")) {
            deleteLastCharacter();
        } else if (buttonText.equals("√")) {
            calcSquareRoot();
        } else if (buttonText.equals("+/-")) {
            changeSign();
        }
    }
    private void handleNumberInput(String buttonText) {
        if (isCalculatedValue) {
            currentInput.setLength(0);
            isCalculatedValue = false;
        }
        currentInput.append(buttonText);
        updateDisplay();
    }
    private void handleOperatorInput(String buttonText) {
        isCalculatedValue = false;
        currentInput.append(" " + buttonText + " ");
        updateDisplay();
    }
    private void updateDisplay() {
        Screen.setText(currentInput.toString());
    }
    private void clearAll() {
        currentInput.setLength(0);
        result = 0;
        isCalculatedValue=false;
        updateDisplay();
    }
    private void calculateResult() {
        if (currentInput.length() > 0) {
            String expression = currentInput.toString();

            String[] parts = expression.split(" ");
            if (parts.length != 3) {
                handleInvalidExpression();
                return;
            }
            try {
                double num1 = Double.parseDouble(parts[0]);
                char operator = parts[1].charAt(0);
                double num2 = Double.parseDouble(parts[2]);

                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            handleDivisionByZero();
                            return;
                        }
                        break;
                    default:
                        handleInvalidOperator();
                        return;
                }

                currentInput.setLength(0);
                currentInput.append(formatValue(result));
                isCalculatedValue=true;
                updateDisplay();
            } catch (NumberFormatException e) {
                handleInvalidNumberFormat();
            }
        }
    }
    private void handleInvalidExpression(){
        currentInput.setLength(0);
        currentInput.append("Error: Invalid Expression");
        updateDisplay();
    }
    private void handleDivisionByZero(){
        currentInput.setLength(0);
        currentInput.append("Error: Division by Zero");
        updateDisplay();
    }
    private void handleInvalidOperator() {
        currentInput.setLength(0);
        currentInput.append("Error: Invalid Operator");
        updateDisplay();
    }
    private void handleInvalidNumberFormat() {
        currentInput.setLength(0);
        currentInput.append("Error: Invalid Number Format");
        updateDisplay();
    }
    private void deleteLastCharacter() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            updateDisplay();
        }
    }
    private void calcSquareRoot() {
        if (currentInput.length() > 0) {
            double value = Double.parseDouble(currentInput.toString());
            result = Math.sqrt(value);
            currentInput.setLength(0);
            currentInput.append(formatValue(result));
            isCalculatedValue = true;
            updateDisplay();
        }
    }
    private void changeSign() {
        if (currentInput.length() > 0) {
            try {
                double currentValue = Double.parseDouble(currentInput.toString());
                double newValue = -currentValue;
                currentInput.setLength(0);
                currentInput.append(formatValue(newValue));
                updateDisplay();
            } catch (NumberFormatException e) {
                handleInvalidNumberFormat();
            }
        }
    }
    private String formatValue(double value){
        return decimalFormat.format(value);
    }
}