package edu.virginia.cs4720.final_project_final_sudowoodo;

import android.media.Image;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Item {

    private String name;
    private String category;
    private double amt;
    private String details;
    private Image pic;

    private Date date;
    private int year;
    private int month;
    private int day;

    public Item (String name, String category, double amt, String details, int year, int month, int day ){
        this.name=name;
        this.category=category;
        this.amt=amt;
        this.details=details;
        this.year=year;
        this.month=month;
        this.day=day;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}
