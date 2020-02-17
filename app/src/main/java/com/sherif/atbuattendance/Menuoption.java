package com.sherif.atbuattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class Menuoption extends AppCompatActivity {
    ProgressDialog pd;
    Intent intent;
    AlertDialog.Builder builder;
    SharedPreferences MyName,MyDept,MyId;

    private static int SPLASH_TIME_OUT = 500;//5seconds
    TextView txtname;
    URLConnection urlconnection;
    URL url;
    public List<phoneList> arraylist;
    //String address = "http://192.168.230.1/attendance/androidNamsn.php";
    String address = "http://192.168.43.70/attendance/androidNamsn.php";
    String fullname,alldataStr;
    private boolean isConnected = false;
    String dept,User_id;

    private SQLiteDatabase mDb;
    private dbHelper dbHelper;
    Button retrieve,upload;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuoption);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new dbHelper(this);
         retrieve = (Button)findViewById(R.id.attendance);
         upload = (Button)findViewById(R.id.upload);
        Button close = (Button)findViewById(R.id.close);
        arraylist = new ArrayList<>();
        txtname= (TextView) findViewById(R.id.name);

        MyName = this.getSharedPreferences("MyName", this.MODE_PRIVATE);
        MyDept = this.getSharedPreferences("MyDept", this.MODE_PRIVATE);
        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);


        dept = MyDept.getString("MyDept", "");
        fullname = MyName.getString("MyName", "");
        User_id = MyId.getString("MyId", "");

        txtname.setText(fullname);

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading Courses ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        retrieve.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Courses.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              verify_close();
            }
        });

        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                displaySaveMsg();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new testConnection().execute();
            }
        }, SPLASH_TIME_OUT);
    }
public void displaySaveMsg(){
    String msg="Confirmation: Are You Sure You Want To Upload Attendance Record To Server ? " + System.getProperty("line.separator")
            + "Please Confirm !";
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new testConnectionload().execute();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }
    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();;
        switch (id){
            case R.id.attendancem:
                intent = new Intent(getApplicationContext(), Courses.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.uploadm:
                displaySaveMsg();
                break;
            case R.id.close:
                verify_close();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void verify_close(){
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Really Want to Exit ATBU E-ATTENDANCE... ?. ");
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //EMPTY SOME OF THE SHARED PREFERANCES

                SharedPreferences.Editor editor;
                editor = MyId.edit();
                editor.putString("MyId", "");
                editor.apply();
                //name
                editor = MyName.edit();
                editor.putString("MyName", "");
                editor.apply();
                //department
                editor = MyDept.edit();
                editor.putString("MyDept", "");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }
    public void displayMessage(String msg) {
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

    @Override
    public void onBackPressed() {
        verify_close();
    }

    //if network available
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            isConnected = true;
                        }
                        return true;
                    }
                }
            }
        }
        isConnected = false;
        return false;
    }
    class testConnection extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            if(pd.isShowing()){
                pd.hide();
            }
            pd.show();
            super.onPreExecute();
        }

        boolean outre;
        @Override
        protected String doInBackground(String... strings) {
            outre = isNetworkAvailable(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            if(outre) {
               volleyJsonArrayRequest(address);
            }else{
                volleyJsonArrayRequest(address);
            }
        }
    }

    //volley request
    public void volleyJsonArrayRequest(String url) {
        String REQUEST_TAG = "com.volley.volleyJsonArrayRequestSearched";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = null;
                            JSONArray jsonarray = new JSONArray(response);
                            jsonobject = jsonarray.getJSONObject(0);
                            String error = jsonobject.getString("Error");
                            if (error.equals("Error: No Record Of Course !!!")) {
                                if(pd.isShowing()){
                                    pd.hide();
                                }
                                retrieve.setEnabled(false);
                               displayMessage("Error: No Course Registered For Lecturer Yet !!!");

                            }else{
                                //displayMessage("ssssss: No Course Registered For Lecturer Yet !!!"  +  error);
                                retrieve.setEnabled(true);
                               loadValues(response);
                            }
                        } catch (JSONException e) {
                            if(pd.isShowing()){
                                pd.hide();
                            }
                           displayMessage("Error: Fail To Retrieve Record !!!" + e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        displayMessage("Error: No Internet Connection In Your Device ..!!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "retrievecourse");
                params.put("userID", User_id);
                return params;
            }
        };
        AppSingleton.getInstance(this).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    //internet result load result
    public void loadValues(String comingNews) {
        try {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;
            jsonarray = new JSONArray(comingNews);
            arraylist.clear();
            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                String title = jsonobject.getString("title");
                String unit = jsonobject.getString("unit");
                String code = jsonobject.getString("code");
                dbHelper.addNewCourse(code,title,unit);
            }
            if(pd.isShowing()){
                pd.hide();
            }
        } catch (JSONException e) {
            if(pd.isShowing()){
                pd.hide();
            }
            e.printStackTrace();
        }
    }

    //upload register to online server starts here
    class testConnectionload extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            if(pd.isShowing()){
                pd.hide();
            }
            pd.show();
            super.onPreExecute();
        }

        boolean outre;
        @Override
        protected String doInBackground(String... strings) {
            outre = isNetworkAvailable(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            if(outre) {
                loadlocalData(address);
            }else{
                /**if(pd.isShowing()){
                    pd.hide();
                }
                displayMessage("Error: No Internet Connection In Your Device ..!!");**/
                loadlocalData(address);
            }
        }
    }
    //load all record in local db if any exist
    public void loadlocalData(String address){
        Cursor cursor = dbHelper.getAllAttendance();
        if(cursor.getCount() >= 1) {
            int k = 1;
            JSONArray jsonarray = new JSONArray();
            while (cursor.moveToNext()) {
                JSONObject record = new JSONObject();

                try {
                    record.put("stName",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_STUDNAME)));
                    record.put("stReg",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_REGNO)));
                    record.put("stCode",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_COURSECODE)));
                    record.put("stDate",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_DATE)));
                    record.put("stLevel",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_LEVEL)));
                    record.put("stID",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_RECORDID)));
                    record.put("stDept",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_DEPT)));
                    record.put("stFac",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_FACULTY)));
                    jsonarray.put(record);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject allrecord = new JSONObject();
            try {
                allrecord.put("allrecord",jsonarray);
                alldataStr = allrecord.toString();
                //send record to server
                volleyJsonArrayRequestR(address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if(pd.isShowing()){
                pd.hide();
            }
            displayMessage("Error: There Is No Record Of Attendance Available In The Server ..!!");
        }
    }
    //volley request
    public void volleyJsonArrayRequestR(String url) {
        String REQUEST_TAG = "com.volley.volleyJsonArrayRequestSave";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String error = jsonobject.getString("Error");
                            if(error.equals("Error")){
                                displayMessage("Success: Register Wass Uploaded to Server Successfully !!!");
                                emptyAttendace();
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayMessage("Error: Unable to Upload Register Record to Server. Retry !!!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        displayMessage("Error: Unable to Upload Register Record to Server. Retry !!!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "uploaddata");
                params.put("record", alldataStr);
                return params;
            }
        };
        AppSingleton.getInstance(this).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    //delete all record in database
    public void emptyAttendace(){
        dbHelper.deleteAttendace();
    }
}
