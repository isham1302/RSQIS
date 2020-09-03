package com.example.rsqis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {
    EditText editText;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        mSubmit= findViewById(R.id.button_submit);
        editText= findViewById(R.id.email);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
                Toast.makeText(ForgotPassword.this, "Request sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validateEmail(){
        String emailInput= editText.getText().toString().trim();
        if (emailInput.isEmpty()){
           editText.setError("Field can't be empty");
           return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            editText.setError("Please enter a valid email address");
            return false;
        }else {
            editText.setError(null);
            return true;
        }

    }

}
