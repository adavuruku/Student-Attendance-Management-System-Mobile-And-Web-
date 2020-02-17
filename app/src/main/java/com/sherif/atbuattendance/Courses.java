package com.sherif.atbuattendance;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Courses extends AppCompatActivity {
    List<phoneList> arraylist;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    Intent intent;
    RecyclerView recyclerView;
    SharedPreferences MyName, MyDept, MyId;
    String dept, User_id, fullname,coursecode,coursetitle;
    private phoneAdapter phoneAdapter;
    private dbHelper dbHelper;
    private boolean isConnected = false;
    Toolbar toolbar;
    String address = "http://192.168.43.70/attendance/androidNamsn.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        arraylist = new ArrayList<>();
        //phoneAdapter = new phoneAdapter(recyclerView, arraylist, this);
        //recyclerView.setAdapter(phoneAdapter);

        MyName = this.getSharedPreferences("MyName", this.MODE_PRIVATE);
        MyDept = this.getSharedPreferences("MyDept", this.MODE_PRIVATE);
        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);

        dept = MyDept.getString("MyDept", "");
        fullname = MyName.getString("MyName", "");
        User_id = MyId.getString("MyId", "");
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading Courses ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new loadLocalData().execute();
            }
        }, 300);
    }

    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();;
        switch (id){
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Menuoption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menuoption.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    //load SQLite Data
    class loadLocalData extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            // connectionStatus.setText("No Internet here !!" + userID);

            dbHelper = new dbHelper(getApplicationContext());
            Cursor cursor = dbHelper.getAllCourses();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String code = cursor.getString(cursor.getColumnIndex(dbColumnList.userCourses.COLUMN_COURSECODE));
                    String unit = cursor.getString(cursor.getColumnIndex(dbColumnList.userCourses.COLUMN_COURSEUNIT));
                    String title = cursor.getString(cursor.getColumnIndex(dbColumnList.userCourses.COLUMN_COURSETITLE));
                    arraylist.add(new phoneList(unit, title, code));
                }
            }else{
                Toast.makeText(getApplicationContext(),"No Record Found !!!", Toast.LENGTH_SHORT).show();
            }


            phoneAdapter = new phoneAdapter(arraylist, getApplication(), new phoneAdapter.OnItemClickListener() {
                @Override
                public void onCallClick(View v, int position) {
                    coursecode = arraylist.get(position).getCode();
                    coursetitle = arraylist.get(position).getTitle();
                    new testConnection().execute();
                }
            });
            phoneAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(phoneAdapter);
        }
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
        String REQUEST_TAG = "com.volley.volleyJsonArrayRequestUser";
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
                                checkLocal(coursecode);

                            }else{
                                loadValues(response);
                            }
                        } catch (JSONException e) {
                            checkLocal(coursecode);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       checkLocal(coursecode);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "retrievestudent");
                params.put("coursecode", coursecode);
                return params;
            }
        };
        AppSingleton.getInstance(this).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    //isert new register student for courses
    //internet result load result
    public void loadValues(String comingNews) {
        try {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;
            jsonarray = new JSONArray(comingNews);
            arraylist.clear();
            for (int i = 0; i < jsonarray.length(); i++) {
                jsonobject = jsonarray.getJSONObject(i);
                String dept = jsonobject.getString("dept");
                String fac = jsonobject.getString("fac");
                String code = jsonobject.getString("code");
                String stname = jsonobject.getString("stname");
                String level = jsonobject.getString("level");
                String regno = jsonobject.getString("regno");
                dbHelper.addNewStudent(code,stname,regno,level,dept,fac);
            }
            openCourseList(coursecode,coursetitle);
        } catch (JSONException e) {
            if(pd.isShowing()){
                pd.hide();
            }
            e.printStackTrace();
        }
    }

    //check if stud has been uploaded before -
    // if so open register else - show no student register yet
    public void checkLocal(String comingNews) {
        dbHelper = new dbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getStudlist(comingNews);
        if(cursor.getCount() >=1){
            cursor.close();
            openCourseList(coursecode,coursetitle);
        }else{
            if(pd.isShowing()){
                pd.hide();
            }
            displayMessage("No Student Has Registered For "+ comingNews +" Yet !!!");
        }
    }

    public void openCourseList(String course,String title) {
        if(pd.isShowing()){
            pd.hide();
        }
        intent = new Intent(getApplicationContext(), attendance.class);
        intent.putExtra("coursecode", course);
        intent.putExtra("ctitle", title);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }
}
