package com.bit_nation.sadaqareminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_DIGITS_BEFORE_DOT = 7;
    private static final int MAX_DIGITS_AFTER_DOT = 2;

    private double mAmountDue;

    private TextView mSadaqaAmountTextView;
    private Button mSubmitAmountButton;
    private EditText mEnterAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAmountDue = 0.0;

        mSadaqaAmountTextView = findViewById(R.id.sadaqa_amount_textView);
        mSubmitAmountButton = findViewById(R.id.add_amount_button);
        mEnterAmountEditText = findViewById(R.id.enter_amount_editText);

        mEnterAmountEditText.setFilters(new InputFilter[]{new DigitsInputFilter(MAX_DIGITS_BEFORE_DOT, MAX_DIGITS_AFTER_DOT, Double.MAX_VALUE)});

        setSadaqaAmountTextView();
    }

    private void setSadaqaAmountTextView() {
        mSadaqaAmountTextView.setText(NumberFormat.getCurrencyInstance().format(mAmountDue));
    }

    public void addAmount(View view) {
        double value = parseDouble(mEnterAmountEditText.getText().toString());

        mAmountDue += value;

        setSadaqaAmountTextView();
    }

    public void removeAmount(View view) {
        double value = parseDouble(mEnterAmountEditText.getText().toString());

        mAmountDue -= value;

        setSadaqaAmountTextView();
    }

    private double parseDouble(String s){
        if (s == null || s.isEmpty()) {
            return 0.0;
        }
        else {
            return Double.parseDouble(s);
        }
    }
}
