package com.revuc.speedacm.ucwhatididthere.util;

import java.util.List;

/**
 * Created by samnwosu on 10/8/16.
 */

public class User {
    private String mUUID;
    private String mEmail;
    private List<Stamp> mStamps;

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String UUID) {
        mUUID = UUID;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public List<Stamp> getStamps() {
        return mStamps;
    }

    public void setStamps(List<Stamp> stamps) {
        mStamps = stamps;
    }




    public User(String UUID, String email, List<Stamp> stamps) {
        mUUID = UUID;
        mEmail = email;
        mStamps = stamps;
    }

}
