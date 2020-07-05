package com.example.studentsapp;

import java.util.Date;

public class Course {

    private String AttendanceCount;
    private Date Time;
    private String Cname;
    private String CID;
    private String Key;

    public String getCourseTimeOut() {
        return CourseTimeOut;
    }

    public void setCourseTimeOut(String courseTimeOut) {
        CourseTimeOut = courseTimeOut;
    }

    private String CourseTimeOut;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String email;
    private String password;
    private String phone;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public Course() {
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getAttendanceCount() {
        return AttendanceCount;
    }

    public void setAttendanceCount(String attendanceCount) {
        AttendanceCount = attendanceCount;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public String getFullInfo(){
        return "" + String.valueOf(AttendanceCount).concat(" ".concat(Key.concat(" ").concat(Time.toString())));
    }
}
