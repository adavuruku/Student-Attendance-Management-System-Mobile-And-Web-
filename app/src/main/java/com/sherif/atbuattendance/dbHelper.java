package com.sherif.atbuattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbHelper extends SQLiteOpenHelper {
    // Database Info
    // Database Info
    public static final String DATABASE_NAME = "ATBUSERVER.db";
    public static final String DBLOCATION = "/data/data/com.example.atbuattendance/databases/";
    private static final int DATABASE_VERSION = 1;
    private Context mcontext;
    private SQLiteDatabase mdatabase;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS " + dbColumnList.myAccount.TABLE_NAME +
                "(" +
                    dbColumnList.myAccount._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                    dbColumnList.myAccount.COLUMN_DEPT + " VARCHAR, " +
                    dbColumnList.myAccount.COLUMN_NAME + " TEXT, " +
                    dbColumnList.myAccount.COLUMN_PASSWORD + " VARCHAR, " +
                    dbColumnList.myAccount.COLUMN_FACULTY + " VARCHAR, " +
                    dbColumnList.myAccount.COLUMN_USERID + " VARCHAR " +
                ")";

        String CREATE_TABLE_ATTENDANCE = "CREATE TABLE IF NOT EXISTS " + dbColumnList.registerList.TABLE_NAME +
                "(" +
                    dbColumnList.registerList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                    dbColumnList.registerList.COLUMN_COURSECODE + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_STUDNAME + " TEXT, " +
                    dbColumnList.registerList.COLUMN_REGNO + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_LEVEL + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_RECORDID + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_DEPT + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_FACULTY + " VARCHAR, " +
                    dbColumnList.registerList.COLUMN_DATE + " VARCHAR" +
                ")";
        String CREATE_TABLE_USERCOURSE = "CREATE TABLE IF NOT EXISTS " + dbColumnList.userCourses.TABLE_NAME +
                "(" +
                dbColumnList.userCourses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.userCourses.COLUMN_COURSECODE + " VARCHAR, " +
                dbColumnList.userCourses.COLUMN_COURSETITLE + " TEXT, " +
                dbColumnList.userCourses.COLUMN_COURSEUNIT + " VARCHAR" +
                ")";
        String CREATE_TABLE_STUDLIST = "CREATE TABLE IF NOT EXISTS " + dbColumnList.studetList.TABLE_NAME +
                "(" +
                dbColumnList.registerList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.registerList.COLUMN_COURSECODE + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_STUDNAME + " TEXT, " +
                dbColumnList.registerList.COLUMN_REGNO + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_LEVEL + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_DEPT + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_FACULTY + " VARCHAR" +
                ")";
        //   sqLiteDatabase.openOrCreateDatabase(DATABASE_NAME,null);

        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);
        sqLiteDatabase.execSQL(CREATE_TABLE_ATTENDANCE);
        sqLiteDatabase.execSQL(CREATE_TABLE_USERCOURSE);
        sqLiteDatabase.execSQL(CREATE_TABLE_STUDLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.myAccount.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.registerList.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userCourses.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.studetList.TABLE_NAME);
            //recreate the tables
            onCreate(sqLiteDatabase);
        }
    }

    public void openDatabase(){
        String dbpath = mcontext.getDatabasePath(DATABASE_NAME).getPath();
        if(mdatabase != null && mdatabase.isOpen()){
            return;
        }
        mdatabase = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDatabase(){
        if(mdatabase != null){
            mdatabase.close();
        }
    }
/*******************************************************************/
    /****** USER DETAILS *********************************************/

    public Cursor getStudlist(String userName){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.studetList.TABLE_NAME,
                null,
                 dbColumnList.studetList.COLUMN_COURSECODE +" = ?",
                new String[]{userName},
                null,
                null,
                null);
    }
//search all user by department
    public Cursor getLogin(String userName, String userPassword){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.myAccount.TABLE_NAME,
                null,
                dbColumnList.myAccount.COLUMN_USERID +" = ? AND " + dbColumnList.myAccount.COLUMN_PASSWORD +" = ?",
                new String[]{userName,userPassword},
                null,
                null,
                null);
    }
    public Cursor getAllAccount(){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.myAccount.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
    public Cursor getAllCourses(){
       // openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userCourses.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
    public Cursor getAllAttendance(){
    //    openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
    //search user by id
    public Cursor getAttendance(String searchField, String contentSearch){
       // openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                searchField +" = ?",
                new String[]{contentSearch},
                null,
                null,
                null);
    }
//delete all attendance
    public void deleteAttendace(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(dbColumnList.registerList.TABLE_NAME,
                null,null);
    }

    //Insert details
    public void addNewAttendance(String coursecode, String studname,String dept,String faculty,String regno,String level,String recordid,String datesub) {
      //  openDatabase();
        // Create and/or open the database for writing
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.registerList.COLUMN_COURSECODE, coursecode);
        cv.put(dbColumnList.registerList.COLUMN_LEVEL, level);
        cv.put(dbColumnList.registerList.COLUMN_RECORDID, recordid);
        cv.put(dbColumnList.registerList.COLUMN_REGNO, regno);
        cv.put(dbColumnList.registerList.COLUMN_STUDNAME, studname);
        cv.put(dbColumnList.registerList.COLUMN_DEPT, dept);
        cv.put(dbColumnList.registerList.COLUMN_FACULTY, faculty);
        cv.put(dbColumnList.registerList.COLUMN_DATE, datesub);
        //cv.put(dbColumnList.userDetails.COLUMN_PICS, byteArray);
        database.insert(dbColumnList.registerList.TABLE_NAME,null,cv);
    }
    //Insert course
    public Cursor verifyCourseExist(String coursecode){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.userCourses.TABLE_NAME + " WHERE "+dbColumnList.userCourses.COLUMN_COURSECODE +"= '" + coursecode +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void addNewCourse(String coursecode, String coursetitle,String courseunit) {
        //openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = verifyCourseExist(coursecode);
        if(cursor.getCount() >= 1) {
            ContentValues values = new ContentValues();
            values.put(dbColumnList.userCourses.COLUMN_COURSEUNIT, courseunit);
            values.put(dbColumnList.userCourses.COLUMN_COURSETITLE, coursetitle);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = database.update(dbColumnList.userCourses.TABLE_NAME, values, dbColumnList.userCourses.COLUMN_COURSECODE + "= ?", new String[]{coursecode});
        }else{
            // Create and/or open the database for writing

            ContentValues cv = new ContentValues();
            cv.put(dbColumnList.userCourses.COLUMN_COURSEUNIT, courseunit);
            cv.put(dbColumnList.userCourses.COLUMN_COURSETITLE, coursetitle);
            cv.put(dbColumnList.userCourses.COLUMN_COURSECODE, coursecode);
            database.insert(dbColumnList.userCourses.TABLE_NAME,null,cv);
        }

    }

    //Insert student for course list
    public Cursor verifyStudExist(String coursecode,String regno){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.studetList.TABLE_NAME + " WHERE "+dbColumnList.studetList.COLUMN_COURSECODE +"= '" + coursecode +"' AND "+dbColumnList.studetList.COLUMN_REGNO + " ='" + regno + "' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void addNewStudent(String coursecode, String studname,String regno,String level,String dept,String faculty) {
        //openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = verifyStudExist(coursecode,regno);
        if(cursor.getCount() >= 1) {
         /**   ContentValues values = new ContentValues();
            values.put(dbColumnList.st.COLUMN_COURSEUNIT, courseunit);
            values.put(dbColumnList.userCourses.COLUMN_COURSETITLE, coursetitle);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = database.update(dbColumnList.userCourses.TABLE_NAME, values, dbColumnList.userCourses.COLUMN_COURSECODE + "= ?", new String[]{coursecode});**/
        }else{
            // Create and/or open the database for writing

            ContentValues cv = new ContentValues();
            cv.put(dbColumnList.studetList.COLUMN_COURSECODE, coursecode);
            cv.put(dbColumnList.studetList.COLUMN_DEPT, dept);
            cv.put(dbColumnList.studetList.COLUMN_FACULTY, faculty);
            cv.put(dbColumnList.studetList.COLUMN_LEVEL, level);
            cv.put(dbColumnList.studetList.COLUMN_REGNO, regno);
            cv.put(dbColumnList.studetList.COLUMN_STUDNAME, studname);

            database.insert(dbColumnList.studetList.TABLE_NAME,null,cv);
        }

    }

    //Insert account
    public Cursor verifyAccountExist(String staffid){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.myAccount.TABLE_NAME + " WHERE "+dbColumnList.myAccount.COLUMN_USERID +"= '" + staffid +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void addNewAccount(String staffid, String staffname,String dept,String pword,String fac) {
        //openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = verifyAccountExist(staffid);
        if(cursor.getCount() >= 1) {
            ContentValues values = new ContentValues();
            values.put(dbColumnList.myAccount.COLUMN_DEPT, dept);
            values.put(dbColumnList.myAccount.COLUMN_NAME, staffname);
            values.put(dbColumnList.myAccount.COLUMN_PASSWORD, pword);
            values.put(dbColumnList.myAccount.COLUMN_FACULTY, fac);
            int rows = database.update(dbColumnList.myAccount.TABLE_NAME, values, dbColumnList.myAccount.COLUMN_USERID + "= ?", new String[]{staffid});
        }else{
            // Create and/or open the database for writing

            ContentValues cv = new ContentValues();
            cv.put(dbColumnList.myAccount.COLUMN_USERID, staffid);
            cv.put(dbColumnList.myAccount.COLUMN_DEPT, dept);
            cv.put(dbColumnList.myAccount.COLUMN_NAME, staffname);
            cv.put(dbColumnList.myAccount.COLUMN_PASSWORD, pword);
            cv.put(dbColumnList.myAccount.COLUMN_FACULTY, fac);
            database.insert(dbColumnList.myAccount.TABLE_NAME,null,cv);
        }

    }
}
