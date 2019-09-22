package com.agri.enquete;

public class GetUserDetail {
    String name;
    String mobile;
    String femail;
    String fdob;



    public GetUserDetail(String name, String mobile, String femail, String fdob) {
        this.name = name;
        this.mobile = mobile;
        this.femail=femail;
        this.fdob=fdob;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFemail() {
        return femail;
    }

    public String getFdob() {
        return fdob;
    }
}