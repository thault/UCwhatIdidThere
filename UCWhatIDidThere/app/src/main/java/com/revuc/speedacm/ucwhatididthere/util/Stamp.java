package com.revuc.speedacm.ucwhatididthere.util;

/**
 * Created by samnwosu on 10/8/16.
 */

public class Stamp {

    private String mUrl;
    private String mGps;
    private String mDiscription;

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

    public Stamp(String url, String gps, String discription, String UUID){
        mUrl = url;
        mGps = gps;
        mDiscription = discription;
        mUUID = UUID;
    }


}
