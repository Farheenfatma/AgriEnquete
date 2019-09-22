package com.agri.enquete.Livemandi;

public class MandiCrops {
    private String userId;
    private String cropname;
    private String imgurl;
    private String quantity;
    private String price;
    private String place;
    private String mobileno;
    private  String name;
    private String locality;


    private String state;


public MandiCrops()
{

}


    public MandiCrops(String cropname, String quantity, String price, String place, String mobileno, String userId, String imgurl,String name,String locality,String state) {
        this.cropname = cropname;
        this.quantity = quantity;
        this.price=price;
        this.place=place;
        this.mobileno=mobileno;
        this.userId=userId;
        this.imgurl=imgurl;
        this.name=name;
        this.locality=locality;
        this.state=state;
//        this.artistGenre = artistGenre;
    }
    public String getCropname() {
        return cropname;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
