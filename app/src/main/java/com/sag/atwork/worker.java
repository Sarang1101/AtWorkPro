package com.sag.atwork;

public class worker {
    public String name ;
    public String location;
    public String profile;
    public String work;
    public String wid;
    public String starRating;


    public worker(String name2, String location2,String work2, String image2,String wid2,String StarRating2) {
        this.name=name2;
        this.location=location2;
        this.work=work2;
        this.profile=image2;
        this.wid=wid2;
        this.starRating=StarRating2;
        // TODO Auto-generated constructor stub
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getWid() {
        return profile;
    }
    public void setWid(String wid) {
        this.wid = wid;
    }
    public String getStarRating() {
        return starRating;
    }
    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }


}

