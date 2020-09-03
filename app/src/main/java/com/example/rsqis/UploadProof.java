package com.example.rsqis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class UploadProof extends AppCompatActivity {


    private static final int PERMISSION_CODE = 1000;
    private static final int DATA_CAPTURE_CODE = 1001;
    private static final int IMAGE_PICK_CODE= 1002;
    private static final int VIDEO_CAPTURE_CODE = 100;

    int PERMISSION_ID = 44;
    SharedPreferences pref ; // 0 - for private mode
    SharedPreferences.Editor editor;
    Uri image_uri;
    CardView card;
    EditText desc;
    Button submit;
    Bitmap bitmap;
    AlertDialog.Builder builder;
    AlertDialog alert;
    String workID,roadID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_proof);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload proof");
        builder = new AlertDialog.Builder(this);
        card=(CardView)findViewById(R.id.card);
        desc=(EditText)findViewById(R.id.desc);
        submit=(Button)findViewById(R.id.submit);
        submit.setEnabled(false);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        roadID= getIntent().getStringExtra("roadID");
        String user=  pref.getString("user", null);
            if(user!=null){

                try {
                    JSONObject obj=new JSONObject(user);
                    workID=obj.getString("userID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,DATA_CAPTURE_CODE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData(bitmap);
                submit.setEnabled(false);
            }
        });

        builder.setMessage("Sucessfully Uploaded")
                .setCancelable(false)

                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        desc.setText("");
                        submit.setEnabled(false);
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        alert = builder.create();
        //Setting the title manually
        alert.setTitle("Great Job!");



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==DATA_CAPTURE_CODE)
        {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    submit.setEnabled(true);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadProof.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void openCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"from the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
      //  startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }
    private void pickImageFromGallery(){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }
    private void recordVideo(){
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Video");
        values.put(MediaStore.Images.Media.DESCRIPTION,"from the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(videoIntent, VIDEO_CAPTURE_CODE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:{

                Intent intent= new Intent(this,login.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String imageToString(Bitmap bitmaps) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        String withBase = "data:image/jpeg;base64," + encoded;

        return withBase;



    }



    public void uploadData(final Bitmap bitmap){
        String host = getResources().getString(R.string.host);
        String URLline = host+"uploadProof";




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        alert.show();


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

                    params.put("img",imageToString(bitmap));
                params.put("workID",workID);
                params.put("desc",String.valueOf(desc.getText()));
                params.put("roadID",roadID);


                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

}

