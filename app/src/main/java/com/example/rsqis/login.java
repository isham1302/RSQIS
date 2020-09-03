package com.example.rsqis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    EditText mTextEmail ;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    TextView mPassword;
    SharedPreferences pref ; // 0 - for private mode
    SharedPreferences.Editor editor;

    private static final Pattern Password_Pattern=
            Pattern.compile(".{6,}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        checkConnection();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String user=  pref.getString("user", null);
        if(user!=null){
            JSONObject obj= null;
            try {
                obj = new JSONObject(user);
                if(obj.getString("role").equals("worker")){
                    Intent intent= new Intent(login.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                }else if(obj.getString("role").equals("public")){
                    Intent FeedbackIntent= new Intent(login.this,Feedback.class);
                    startActivity(FeedbackIntent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mTextEmail  = (EditText)findViewById(R.id.edittext_email);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mPassword= (TextView) findViewById(R.id.password);
        mTextViewRegister = (TextView) findViewById(R.id.textview_register);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mTextEmail.getText().toString();
                String pwd=mTextPassword.getText().toString();
                validateEmail();
                validatePassword();

                if (pwd.equals("admin")){
                    Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent FeedbackIntent= new Intent(login.this,Feedback.class);
                    startActivity(FeedbackIntent);
                }
               if (pwd.equals("work")){
                    Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(login.this,Dashboard.class);
                    startActivity(intent);
                }

                else{
                 login(mail,pwd);
                }

            }
        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerwIntent= new Intent(login.this,RegisterActivity.class);
                startActivity(registerwIntent);
            }
        });
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fpIntent= new Intent(login.this,ForgotPassword.class);
                startActivity(fpIntent);
            }
        });



    }

    public void login(final String email, final String password){
        String host = getResources().getString(R.string.host);
        String URLline = host+"login";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putString("user",response);
                        editor.commit(); // commit changes
                        try {
                            JSONObject obj=new JSONObject(response);
                             if(obj.getString("role").equals("worker")){
                                 Intent intent= new Intent(login.this,Dashboard.class);
                                 startActivity(intent);
                                 validateEmail();
                                 //validatePassword();
                                 finish();
                             }else if(obj.getString("role").equals("public")){
                                 Intent FeedbackIntent= new Intent(login.this,Feedback.class);
                                 startActivity(FeedbackIntent);
                                 validateEmail();
                                 finish();
                             } else{
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
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

    private boolean validateEmail(){
        String emailInput= mTextEmail.getText().toString().trim();
        if (emailInput.isEmpty()){
            mTextEmail.setError("Field can't be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            mTextEmail.setError("Please enter a valid email address");
            return false;
        }else {
            mTextEmail.setError(null);
            return true;
        }

    }
   private boolean validatePassword(){
        String passwordInput= mTextPassword.getText().toString().trim();
        if (passwordInput.isEmpty()){
            mTextPassword.setError("Field can't be empty");
            return false;
        }else if(!Password_Pattern.matcher(passwordInput).matches()){
            mTextPassword.setError("Password should contain minimum 6 characters including 1 UpperCase character");
            return false;
        }else {
            mTextPassword.setError(null);
            return true;
        }
    }
    public void checkConnection(){
        ConnectivityManager manager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();

        if (null!=activeNetwork){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "Wifi Connection is enabled", Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Data-Network established", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
