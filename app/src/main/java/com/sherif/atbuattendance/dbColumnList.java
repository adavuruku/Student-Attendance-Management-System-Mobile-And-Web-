package com.sherif.atbuattendance;

import android.provider.BaseColumns;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbColumnList {

    public static class myAccount implements BaseColumns{
        public static final String TABLE_NAME = "myAccount";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_NAME = "MyName";
        public static final String COLUMN_DEPT = "MyDept";
        public static final String COLUMN_PASSWORD = "MyPassword";
        public static final String COLUMN_FACULTY = "MyFac";
    }

    public static class userCourses implements BaseColumns{
        public static final String TABLE_NAME = "userCouses";
        public static final String COLUMN_COURSECODE = "coursecode";
        public static final String COLUMN_COURSETITLE = "coursetitle";
        public static final String COLUMN_COURSEUNIT = "courseunit";
    }

    public static class registerList implements BaseColumns{
        public static final String TABLE_NAME = "registerList";
        public static final String COLUMN_REGNO = "regNo";
        public static final String COLUMN_STUDNAME = "studName";
        public static final String COLUMN_COURSECODE = "courseCode";
        public static final String COLUMN_LEVEL = "courseLevel";
        public static final String COLUMN_DEPT = "courseDept";
        public static final String COLUMN_FACULTY = "coursefac";
        public static final String COLUMN_RECORDID = "recordid";
        public static final String COLUMN_DATE = "recordDate";
    }

    public static class studetList implements BaseColumns {
        public static final String TABLE_NAME = "studetList";
        public static final String COLUMN_REGNO = "regNo";
        public static final String COLUMN_STUDNAME = "studName";
        public static final String COLUMN_COURSECODE = "courseCode";
        public static final String COLUMN_LEVEL = "courseLevel";
        public static final String COLUMN_DEPT = "courseDept";
        public static final String COLUMN_FACULTY = "coursefac";
    }
}
