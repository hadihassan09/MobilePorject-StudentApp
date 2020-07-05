package com.example.studentsapp;
import java.util.ArrayList;

public class Student {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;
    private String SID;

    private ArrayList<Course> Courses;

    public Student() {
        Courses = new ArrayList<>();
    }

    public void AddCourse(Course S){
        Courses.add(S);
    }

    public Course getCourse(String ID){
        for (Course S: Courses){
            if(S.getCID().equals(ID))
                return S;
        }
        return null;
    }



    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", SID='" + SID + '\'' +
                ", Courses=" + Courses +
                '}';
    }
}
