package com.sherif.atbuattendance;

/**
 * Created by sherif146 on 06/04/2017.
 */
public class attendanceList {

        private String fullname;
        private String level;
        private String dept;
        private String code;
        private String regno;
        private String fac;
        private String status;
    public attendanceList(String fullname,String level, String dept,String regno,String fac, String status,String code){
        this.fullname = fullname;
        this.level = level;
        this.code = code;
        this.dept = dept;
        this.regno = regno;
        this.fac = fac;
        this.status = status;
    }
    public String getName(){
        return fullname;
    }
    public void setName(String fullname){
        this.fullname = fullname;
    }

        public String getRegno(){
            return regno;
        }
        public void setRegno(String regno){
            this.regno = regno;
        }

        public String getCode(){
            return code;
        }
        public void setCode(String code){
            this.code = code;
        }

        public String getLevel(){
            return level;
        }
        public void setLevel(String level){
            this.level = level;
        }

        public String getDept (){
            return dept;
        }
        public void setDept (String dept ){
            this.dept  = dept;
        }

        public String getFaculty (){
            return fac;
        }
        public void setFaculty (String fac ){
            this.fac  = fac;
        }

        public String getStatus (){
            return status;
        }
        public void setStatus (String status ){
        this.status  = status;
    }

    }
