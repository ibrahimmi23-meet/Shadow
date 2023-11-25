package com.example.shadow;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Find the Switch in the layout
        Switch adminSwitch = findViewById(R.id.adminSwitch);

        // Set an OnCheckedChangeListener to detect when the switch state changes
        adminSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch state change
                if (isChecked) {
                    // The switch is ON
                    // Implement the logic for the ON state
                    // For example, enable admin features
                } else {
                    // The switch is OFF
                    // Implement the logic for the OFF state
                    // For example, disable admin features
                }
            }
        });
    }
}
