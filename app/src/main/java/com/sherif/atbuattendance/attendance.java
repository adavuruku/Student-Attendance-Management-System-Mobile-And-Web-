package com.sherif.atbuattendance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class attendance extends AppCompatActivity {
    List<attendanceList> arraylist;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    Intent intent;
    RecyclerView recyclerView;
    SharedPreferences MyName, MyDept, MyId;
    String dept, User_id, fullname,coursecode;
    private attendanceAdapter attendanceAdapter;
    private dbHelper dbHelper;
    private boolean isConnected = false;
    TextView lecname;
    Toolbar toolbar;
    String address = "http://192.168.43.70/attendance/androidNamsn.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        arraylist = new ArrayList<>();

        Intent intent = getIntent();
        coursecode = intent.getStringExtra("coursecode");
        String ctitle =intent.getStringExtra("ctitle");

        MyName = this.getSharedPreferences("MyName", this.MODE_PRIVATE);
        MyDept = this.getSharedPreferences("MyDept", this.MODE_PRIVATE);
        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);

        dept = MyDept.getString("MyDept", "");
        fullname = MyName.getString("MyName", "");
        User_id = MyId.getString("MyId", "");

        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
        String date_r = df.format(Calendar.getInstance().getTime());

        lecname = (TextView)findViewById(R.id.lecname);
        String comb =  fullname + System.getProperty("line.separator") + coursecode +" - "+ ctitle + System.getProperty("line.separator") + "Attendance List - " + date_r;
        lecname.setText(comb);

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
        getMenuInflater().inflate(R.menu.menu_attendance, menu);
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
            case R.id.submit:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Save / Upload this Attendance To the Server ... ?. ");
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submitToLocalServe();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert = builder.create();
                alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                alert.show();
                 break;
            case R.id.home:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Exit ATTENDANCE Without Saving Record ... ?. ");
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Menuoption.class);
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
                alert = builder.create();
                alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                alert.show();
                break;
            case R.id.list:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Exit ATTENDANCE Without Saving Record ... ?. ");
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Courses.class);
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
                alert = builder.create();
                alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                alert.show();
                break;
            case R.id.close:
                verify_close();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog  alert;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want to Exit ATTENDANCE Without Saving Record ... ?. ");
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), Courses.class);
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

        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();

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

    public void submitToLocalServe(){
        if(pd.isShowing()){
            pd.hide();
        }
        pd.show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String recid = df.format(Calendar.getInstance().getTime());
        dbHelper = new dbHelper(getApplicationContext());

        for(int i=0; i< arraylist.size();i++){
            String sta =  arraylist.get(i).getStatus();
            if(sta.equals("1")){
                String code =  arraylist.get(i).getCode();
                String dept =  arraylist.get(i).getDept();
                String fac =  arraylist.get(i).getFaculty();
                String level =  arraylist.get(i).getLevel();
                String stname =  arraylist.get(i).getName();
                String regno =  arraylist.get(i).getRegno();
                dbHelper.addNewAttendance(code,stname,dept,fac,regno,level,recid,recid);
            }
        }
        if(pd.isShowing()){
            pd.hide();
        }
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Success: Attendance Uploaded Successfully.. !!!");
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
        Intent intent = new Intent(getApplicationContext(), Courses.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
            Cursor cursor = dbHelper.getStudlist(coursecode);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String code = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_COURSECODE));
                    String fullname = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_STUDNAME));
                    String level = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_LEVEL));
                    String dept = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_DEPT));
                    String regno = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_REGNO));
                    String fac = cursor.getString(cursor.getColumnIndex(dbColumnList.studetList.COLUMN_FACULTY));
                    String status = "0";
                    arraylist.add(new attendanceList(fullname, level, dept,regno,fac,status,code));
                }
            }else{
                Toast.makeText(getApplicationContext(),"No Record Found !!!", Toast.LENGTH_SHORT).show();
            }


            attendanceAdapter = new attendanceAdapter(arraylist, getApplication(), new attendanceAdapter.OnItemClickListener() {
                @Override
                public void onCallClick(View v, int position) {
                    String sta =  arraylist.get(position).getStatus();
                    if(sta.equals("1")){
                        arraylist.get(position).setStatus("0");
                    }else{
                        arraylist.get(position).setStatus("1");
                    }

                    //new testConnection().execute();
                }
            });
            attendanceAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(attendanceAdapter);
        }
    }
}
