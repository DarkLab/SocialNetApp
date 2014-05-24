package com.darklab.socialnetapp;

import android.content.Context;

public class AppVariables {
	private static AppVariables sAppVariables;
	private Context mAppContext;
	
	private String mUserId, mPassword, mCurrentProvider, mCurrentIdentifier;
	
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

	public String getCurrentProvider() {
		return mCurrentProvider;
	}

	public void setCurrentProvider(String currentProvider) {
		mCurrentProvider = currentProvider;
	}

	public String getCurrentIdentifier() {
		return mCurrentIdentifier;
	}

	public void setCurrentIdentifier(String currentIdentifier) {
		mCurrentIdentifier = currentIdentifier;
	}
	
	public String getCurrentProfile(){
		return mCurrentProvider + "-" + mCurrentIdentifier;
	}

}
