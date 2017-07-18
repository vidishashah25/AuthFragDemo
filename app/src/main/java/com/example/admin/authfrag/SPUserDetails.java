package com.example.admin.authfrag;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 2/22/2017.
 */

public class SPUserDetails {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    Context _context;

    private String KEY_USER_NAME = "USER_NAME";

    private String KEY_USER_MOB = "USER_MOB";

    public SPUserDetails(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences("USER_DETAILS",0);
        editor = pref.edit();
    }

    public String getKEY_USER_NAME(){
        return pref.getString(KEY_USER_NAME,"Guest User");
    }

    public void setKEY_USER_NAME(String user_name){
        editor.putString(KEY_USER_NAME,user_name);
        editor.commit();
    }

    public String getKEY_USER_MOB(){
        return pref.getString(KEY_USER_MOB,"Guest Number");
    }

    public void setKEY_USER_MOB(String user_mob){
        editor.putString(KEY_USER_MOB,user_mob);
        editor.commit();

    }


}
