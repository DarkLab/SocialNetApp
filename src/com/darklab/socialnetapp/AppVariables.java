package com.darklab.socialnetapp;

import android.content.Context;

public class AppVariables {
	private static AppVariables sAppVariables;
	private Context mAppContext;
	
	private String mUserId, mPassword, mCurrentProfile;
	
	private AppVariables(Context context){
		mAppContext = context;
		mUserId = "";
		mPassword = "";
	}
	
	public static AppVariables get(Context c){
		if (sAppVariables == null) {
			sAppVariables = new AppVariables(c.getApplicationContext());
		}
		return sAppVariables;
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		mUserId = userId;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public String getCurrentProfile() {
		return mCurrentProfile;
	}

	public void setCurrentProfile(String currentProfile) {
		mCurrentProfile = currentProfile;
	}

}
