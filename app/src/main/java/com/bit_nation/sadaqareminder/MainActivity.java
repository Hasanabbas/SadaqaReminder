package com.bit_nation.sadaqareminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    /**
     * Constant value representing the maximum sadaqa amount
     * before the decimal point that the user can enter
     */
    private static final int MAX_DIGITS_BEFORE_DOT = 7;

    /**
     * Constant value representing the maximum sadaqa amount
     * before the decimal point that the user can enter
     */
    private static final int MAX_DIGITS_AFTER_DOT = 2;

    /**
     * Constant value used for when the user has clicked the Add Button
     */
    private static final int MODE_ADD = 0;

    /**
     * Constant value used for when the user has clicked the Remove Button
     */
    private static final int MODE_REMOVE = 1;

    /**
     * Constant key for saving the amount due
     */
    private static final String AMOUNT_KEY = "Amount due";

    /**
     * Instance variable for saving the amount due
     */
    private SharedPreferences mPreferences;

    /**
     * To store the sadaqa amount that is due
     */
    private double mAmountDue;

    /**
     * A {@link TextView} which holds the amount due by the user
     */
    private TextView mSadaqaAmountTextView;

    /**
     * A {@link Button} which references the button which removes from the amount due
     */
    private Button mRemoveAmountButton;

    /**
     * A {@link Button} which references the button which removes the full amount due
     */
    private Button mRemoveAllAmountButton;

    /**
     * An {@link EditText} which references the field where the user
     * can enter an amount to either add or remove from the amount due
     */
    private EditText mEnterAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We only have one shared preferences file, so retrieve the default one
        mPreferences = this.getPreferences(MODE_PRIVATE);

        // Initialise the amount due to 0 for now
        mAmountDue = getAmount();

        // Find all the views that may have their UI changed
        mSadaqaAmountTextView = findViewById(R.id.sadaqa_amount_textView);
        mEnterAmountEditText = findViewById(R.id.enter_amount_editText);
        mRemoveAmountButton = findViewById(R.id.remove_amount_button);
        mRemoveAllAmountButton = findViewById(R.id.remove_all_amount_button);

        // Set filer on the enter amount EditText
        mEnterAmountEditText.setFilters(new InputFilter[]{new DigitsInputFilter(MAX_DIGITS_BEFORE_DOT, MAX_DIGITS_AFTER_DOT, Double.MAX_VALUE)});

        // Set the amount due view to the current value
        setSadaqaAmountTextView();
    }

    /**
     * This method saves the new amount due to the shared preferences file
     */
    private void saveAmount() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(AMOUNT_KEY, Double.doubleToRawLongBits(mAmountDue));
        editor.apply();
    }

    /**
     * This method retrieves the amount due from the shared preferences file
     */
    private double getAmount() {
        return Double.longBitsToDouble(mPreferences.getLong(AMOUNT_KEY, 0));
    }

    /**
     * Displays the sadaqa amount that the user owes. If the amount due is 0 or less,
     * the amount is set to 0 and the Remove button is disabled.
     */
    private void setSadaqaAmountTextView() {
        // Update the amount due TextView to show the amount owed.
        // We get the currency instance to display the amount in the user's currency
        mSadaqaAmountTextView.setText(NumberFormat.getCurrencyInstance().format(mAmountDue));
    }

    /**
     * This method is called when the Add button is clicked.
     *
     * @param view is a reference to the Add {@link Button} view
     */
    public void addAmount(View view) {
        // Parse the amount that has been entered in the EditText field and save it in a double variable
        double amount = parseDouble(mEnterAmountEditText.getText().toString());
        changeAmount(MODE_ADD, amount);
    }

    /**
     * This method is called when the Remove button is clicked.
     *
     * @param view is a reference to the Remove {@link Button} view
     */
    public void removeAmount(View view) {
        // Parse the amount that has been entered in the EditText field and save it in a variable
        double amount = parseDouble(mEnterAmountEditText.getText().toString());
        changeAmount(MODE_REMOVE, amount);
    }

    public void removeAllAmount(View view) {
        changeAmount(MODE_REMOVE, mAmountDue);
    }

    /**
     * Helper method which handles both adding and removing to/from the amount due
     *
     * @param mode corresponds to which Button has been pressed
     * @param amount to be added or removed
     */
    public void changeAmount(int mode, double amount) {
        // If user entered 0 or nothing, don't do anything
        if (amount == 0.0) {
            return;
        }

        // Check the mode
        switch (mode) {
            case MODE_ADD:
                // Add the value entered by the user to the amount due variable
                mAmountDue += amount;
                // Enable the Remove and Remove All Buttons since the amount due is positive
                mRemoveAmountButton.setEnabled(true);
                mRemoveAllAmountButton.setEnabled(true);
                break;
            case MODE_REMOVE:
                // If the user wants to remove more than the amount due
                if (amount >= mAmountDue) {
                    // Set the amount due variable to 0
                    mAmountDue = 0;
                    // Disable the Remove and Remove All Buttons since there is no more amount due
                    mRemoveAmountButton.setEnabled(false);
                    mRemoveAllAmountButton.setEnabled(false);
                } else {
                    // Minus the amount entered to the amount due variable
                    mAmountDue -= amount;
                }
                break;
        }

        // Save the new amount to SharedPreferences
        saveAmount();
        // Update the TextView showing the amount due
        setSadaqaAmountTextView();
    }

    /**
     * This method parses the value entered by the user in the {@link EditText} field
     *
     * @param s is the string to be parsed
     * @return the number entered as a double or 0.0 if the string is not a valid number
     */
    private double parseDouble(String s){
        // If the string is null or empty return 0
        if (s == null || s.isEmpty()) {
            return 0.0;
        }
        // Otherwise parse the number
        else {
            return Double.parseDouble(s);
        }
    }
}
