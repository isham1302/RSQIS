package com.example.rsqis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;

    SharedPreferences pref ; // 0 - for private mode
    SharedPreferences.Editor editor;
    ListView list;
    String workerID;
    ArrayList<String> mobileArray;
    ArrayList<JSONObject> roadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        roadData=new ArrayList<>();
        String user=pref.getString("user", null);
        if(user==null){
            Intent i=new Intent(Dashboard.this,login.class);
            startActivity(i);
            finish();
        }

        try {
            JSONObject obj=new JSONObject(user);
            workerID=obj.getString("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list=findViewById(R.id.listroad);
           mobileArray=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Work");
            getRoad();

          list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent i=new Intent(Dashboard.this,Map2.class);
                  i.putExtra("data", roadData.get(position).toString());
                  startActivity(i);
                  }
          });

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

                editor.remove("user");
                editor.commit();
                Intent intent= new Intent(this,login.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getRoad(){
        String host = getResources().getString(R.string.host);
        String URLline = host+"getRoad";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr=new JSONArray(response);
                            for(int x=0;x<arr.length();x++){
                                JSONObject obj=new JSONObject(String.valueOf(arr.get(x)));
                                roadData.add(obj);
                                    mobileArray.add(obj.getString("roadName"));

                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(Dashboard.this,
                                    R.layout.list, R.id.item1,mobileArray);
                            list.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Dashboard.this,"NO Work Assign",Toast.LENGTH_LONG).show();
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
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("workerID",workerID);

                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

}
