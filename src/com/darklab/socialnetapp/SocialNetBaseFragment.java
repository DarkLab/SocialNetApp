package com.darklab.socialnetapp;

import android.support.v4.app.Fragment;
import android.util.Log;

public class SocialNetBaseFragment extends Fragment {
	protected static String TAG = "SocialNetBaseFragment";

	protected boolean isValid(String str) {
		return str != null && !str.trim().equals("");
	}

	protected void logResult(String str) {
		Log.i(TAG, "Result: " + str);
	}

	protected void logError(Throwable e) {
		Log.e(TAG, "Error: ", e);
	}

	public SocialNetBaseFragment() {
		super();
	}

}