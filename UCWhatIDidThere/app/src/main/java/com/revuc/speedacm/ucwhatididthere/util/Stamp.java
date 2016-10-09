package com.revuc.speedacm.ucwhatididthere.util;

/**
 * Created by samnwosu on 10/8/16.
 */

public class Stamp {

    private String mUrl;
    private String mGps;
    private String mDiscription;
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getGps() {
        return mGps;
    }

    public void setGps(String gps) {
        mGps = gps;
    }

    public String getDiscription() {
        return mDiscription;
    }

    public void setDiscription(String discription) {
        mDiscription = discription;
    }

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String UUID) {
        mUUID = UUID;
    }

    private String mUUID;

    public Stamp(String name,String url, String gps, String discription, String UUID){
        mName = name;
        mUrl = url;
        mGps = gps;
        mDiscription = discription;
        mUUID = UUID;
    }


}
