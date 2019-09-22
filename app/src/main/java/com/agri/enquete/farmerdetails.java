package com.agri.enquete;

public class farmerdetails {
    public String name, mobile;
    String email;
    String dob;
    String location;
    String locality,state;



    public farmerdetails(String name, String mobile, String email, String dob, String location, String locality, String state) {
        this.name = name;
        this.mobile = mobile;
        this.email=email;
        this.dob=dob;
        this.location=location;
        this.locality=locality;
        this.state=state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String phone) {
        this.mobile = phone;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}