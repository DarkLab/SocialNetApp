package com.darklab.socialnetapp;

import android.content.Context;

public class RequestMaker {
	private static final String TAG = "RequestMaker";

	public static void loginWithSocialId(Context c, ResponceListener listener, String sn,
			String snid) {
		HttpRequest request = new HttpRequest(c);
		request.setListener(listener);
		request.setType(HttpRequest.Type.LoginWithSocialId);
		request.loginWithSocialId(sn, snid);
	}

	public static void findAllUsers(Context c, ResponceListener listener) {
		HttpRequest request = new HttpRequest(c);
		request.setListener(listener);
		request.setType(HttpRequest.Type.FindAllUsers);
		request.findAllUsers();
	}

	public static void createEvent(Context c, ResponceListener listener, String mySn,
			String mySnId, String targetSn, String targetSnId, String feeling,
			String rant, String lat, String lon) {
		HttpRequest request = new HttpRequest(c);
		request.setListener(listener);
		request.setType(HttpRequest.Type.CreateEvent);
		request.createEvent(mySn, mySnId, targetSn, targetSnId, feeling, rant,
				lat, lon);
	}

}
