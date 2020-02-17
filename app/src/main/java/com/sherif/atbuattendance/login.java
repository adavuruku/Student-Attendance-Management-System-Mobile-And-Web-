package com.sherif.atbuattendance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
    Button login;
    TextView userid, password;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    URLConnection urlconnection;
    URL url;
   // String address = "http://192.168.230.1/attendance/androidNamsn.php";
    String address = "http://192.168.43.70/attendance/androidNamsn.php";
    String allResult, userName, userPassword, et1, et2, et3,et4,et6, type;
    SharedPreferences MyName, MyDept, MyId, MyFac;
    private dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userid = (TextView) findViewById(R.id.userid);
        password = (TextView) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnSave);

        //shared pref initialise (session)
        MyName = this.getSharedPreferences("MyName", this.MODE_PRIVATE);
        MyDept = this.getSharedPreferences("MyDept", this.MODE_PRIVATE);
        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);
        MyFac = this.getSharedPreferences("MyFac", this.MODE_PRIVATE);

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);

        //collect login details
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = userid.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
                    displayMessage("Invalid Data's Provided - Please Verify");
                } else {

                    volleyJsonArrayRequest(address);
                   // new testConnection().execute();
                }
            }
        });



    }

    public void displayMessage(String msg) {
      //  pd.hide();
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }


    public void volleyJsonArrayRequest(String url){
        if(pd.isShowing()){
            pd.hide();
        }
        pd.show();
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String error = jsonobject.getString("Error");
                            if(error.equals("Error: Wrong Username Or Password !!!")){
                                new LoginLocal().execute();
                                // displayMessage("Error: Wrong Username Or Password !!!");
                            }else{
                                type= "online";
                                allResult = response;
                                new ReadJSON().execute();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new LoginLocal().execute();
                            //displayMessage("Error: No Internet Connection !!!");
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new LoginLocal().execute();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "login");
                params.put("userID", userName);
                params.put("userPassword", userPassword);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    //login with sqlite
    //login locally
    class LoginLocal extends AsyncTask<String, Integer, String> {
        String outre1;
        @Override
        protected String doInBackground(String... strings) {

            dbHelper = new dbHelper(getApplicationContext());
            Cursor cursor = dbHelper.getLogin(userName,userPassword);

            if(cursor.getCount() >=1){
                cursor.moveToFirst();
                et1 = cursor.getString(cursor.getColumnIndex(dbColumnList.myAccount.COLUMN_NAME));
                et2 = cursor.getString(cursor.getColumnIndex(dbColumnList.myAccount.COLUMN_DEPT));
                et3 = cursor.getString(cursor.getColumnIndex(dbColumnList.myAccount.COLUMN_USERID));
                et4 = cursor.getString(cursor.getColumnIndex(dbColumnList.myAccount.COLUMN_FACULTY));
                et6 = cursor.getString(cursor.getColumnIndex(dbColumnList.myAccount.COLUMN_PASSWORD));
                cursor.close();

                type="local";
                outre1="Yes";
                return null;
            }else{
                outre1="No";
            }
            return outre1;
        }

        @Override
        protected void onPostExecute(String content) {
            if(outre1.equals("Yes")) {
                // new ReadJSON().execute();
                new ReadJSON().execute();
            }
            else{
                if(pd.isShowing()){
                    pd.hide();
                }
                displayMessage("Error: Wrong Username Or Password !!!");
            }
        }
    }


    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                SharedPreferences.Editor editor;
                if(type.equals("online")) {
                    JSONObject jsonobject = new JSONObject(allResult);
                    //must be arranged exact way it comes from server
                    et1 = jsonobject.getString("MyName");
                    et2 = jsonobject.getString("MyDept");
                    et3 = jsonobject.getString("MyId");
                    et4 = jsonobject.getString("MyFac");
                    et6 = jsonobject.getString("MyPassword");
                    dbHelper = new dbHelper(getApplicationContext());
                    dbHelper.addNewAccount(et3,et1,et2,et6,et4);
                }
                //name
                editor = MyName.edit();
                editor.putString("MyName", et1);
                editor.apply();

                //department
                editor = MyDept.edit();
                editor.putString("MyDept", et2);
                editor.apply();

                //userID
                editor = MyId.edit();
                editor.putString("MyId",et3);
                editor.apply();


                //Faculty
                editor = MyFac.edit();
                editor.putString("MyFac",et4);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(pd.isShowing()){
                    pd.hide();
                }
                Toast.makeText(getApplicationContext(),"Welcome "+ et1 + " To ATBU E - ATTENDANCE APP",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), Menuoption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                displayMessage("Error: No Internet Connection !!!");
            }

            super.onPostExecute(s);
        }
    }

    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AlertDialog  alert;
        switch (id){
            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
                break;
            case R.id.close:
                //verify_close();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}//last
