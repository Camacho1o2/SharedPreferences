package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Setting up the name of your shared preferences and key indicator
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT_KEY = "text";
    public static final String SWITCH_KEY = "switch1";

    // Temporary holder of text and switch status as stored in the shared preference
    private String text;
    private boolean switchOnOff;

    // View holders
    TextView tvOutput;
    EditText etInput;
    Button btnSetText;
    Switch saveSwitch;
    Button btnClearData; // Button to clear saved data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencing your views from your layout
        tvOutput = findViewById(R.id.tvOutput);
        etInput = findViewById(R.id.etInput);
        btnSetText = findViewById(R.id.btnSetText);
        saveSwitch = findViewById(R.id.saveSwitch);
        btnClearData = findViewById(R.id.btnClearData); // Reference the new button

        // Load data from SharedPreferences when the activity starts
        loadData();

        // Will save the data on the shared preferences when the switch is toggled
        saveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Do nothing if the switch is checked
                }
            }
        });

        btnSetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                // Update the text view with the new text
                tvOutput.setText(text);
            }
        });

        // Handle the click event of the clear data button
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });

        // Will change the state of the switch if edit text is changed
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveSwitch.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Method to save data to SharedPreferences
    public void saveData() {
        // Checks if the switch is on before saving data
        if (saveSwitch.isChecked()) {
            // Gets the text inputted by the user
            text = etInput.getText().toString();

            // Checks if text is not null to avoid entering null data
            if (!text.equals("")) {
                // Creating the shared preferences and editor
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Actual entering of data(text strings and boolean state of the switch)
                editor.putString(TEXT_KEY, text);
                editor.putBoolean(SWITCH_KEY, true); // Set switch key to true when data is saved
                editor.apply();

                Toast.makeText(this, "Saved to SharedPreferences", Toast.LENGTH_SHORT).show();
            } else {
                saveSwitch.setChecked(false);
                Toast.makeText(this, "Make sure to enter text", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If the switch is off, clear the saved data in SharedPreferences
        }
    }

    // Method to clear the saved data in SharedPreferences
    private void clearData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply(); // Clear all data in SharedPreferences
        Toast.makeText(this, "Data Cleared from SharedPreferences", Toast.LENGTH_SHORT).show();
        loadData(); // Reload the UI after clearing the data
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinishing()) {
            saveData(); // Save data when the app is paused
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (!isFinishing()) {
            saveData(); // Save data when the app is stopped
        }
    }
    // Method to load data from SharedPreferences and update UI
    public void loadData() {
        // Loads the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Gets the data and stores it on a string and boolean variables
        text = sharedPreferences.getString(TEXT_KEY, "No Preferences saved");
        switchOnOff = sharedPreferences.getBoolean(SWITCH_KEY, false);

        // Changes the values and state of textview for output and state of switch
        tvOutput.setText(text);
        saveSwitch.setChecked(switchOnOff);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

