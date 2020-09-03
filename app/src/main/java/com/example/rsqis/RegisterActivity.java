package com.example.rsqis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText mTextFirstname;
    EditText mTextLastname;
    EditText mTextEmail;
    EditText mTextPhone;

    EditText mTextPassword;
    EditText mTextconf;
    Button mButtonResigter;
    TextView mTextViewLogin;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        mTextFirstname = (EditText)findViewById(R.id.edittext_firstname);
        mTextLastname = (EditText)findViewById(R.id.edittext_lastname);
        mTextEmail = (EditText)findViewById(R.id.edittext_email);
        mTextPhone = (EditText)findViewById(R.id.edittext_phone);

        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextconf = (EditText)findViewById(R.id.edittext_conf);
        mButtonResigter = (Button) findViewById(R.id.button_register);
        mTextViewLogin = (TextView) findViewById(R.id.textview_login);

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.edittext_firstname,
                "[A-Za-z]",R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.edittext_lastname,
                "[A-Za-z]" ,R.string.invalid_lastname);
        awesomeValidation.addValidation(this,R.id.edittext_phone,
                "[5-9]{1}[0-9]{9}$",R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.edittext_email,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.edittext_password,
                ".{6,} [A-Z]{1}$",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.edittext_conf,
                R.id.edittext_password,R.string.invalid_conf);

        mButtonResigter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = mTextPassword.getText().toString().trim();
                String conf_pass = mTextconf.getText().toString().trim();
               if (awesomeValidation.validate()){
                   Toast.makeText(RegisterActivity.this, "Registration Successful..", Toast.LENGTH_SHORT).show();
               }
               if (pwd.equals(conf_pass)) {

                    reg();

                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(RegisterActivity.this,login.class);
                startActivity(LoginIntent);

            }
        });



    }
    public void reg(){

        String host = getResources().getString(R.string.host);
        String URLline = host+"register";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj=new JSONObject((response));
                            if(obj.getString("status").equals("200")){
                                Intent LoginIntent = new Intent(RegisterActivity.this,login.class);
                                startActivity(LoginIntent);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("email", String.valueOf(mTextEmail.getText()));
                params.put("ph", String.valueOf(mTextPhone.getText()));
                params.put("fname", String.valueOf(mTextFirstname.getText()));
                params.put("lname", String.valueOf(mTextLastname.getText()));
                params.put("pass", String.valueOf(mTextPassword.getText()));


                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }
}