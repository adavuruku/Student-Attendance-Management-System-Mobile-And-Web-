package com.sherif.atbuattendance;

/**
 * Created by sherif146 on 11/01/2018.
 */

public class phoneList {

    private String unit,title,code;
    public phoneList(String unit,String title, String code){
        this.unit = unit;
        this.title = title;
        this.code = code;
    }
    public String getUnit(){
        return  unit;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }
    public String getCode(){
        return  code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getTitle(){
        return  title;
    }
    public void setTitle(String title){
        this.title = title;
    }
}
