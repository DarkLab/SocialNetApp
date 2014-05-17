package com.darklab.socialnetapp;

public class RequestMaker {
	private static final String TAG = "RequestMaker";

	public static void loginWithSocialId(ResponceListener listener, String sn,
			String snid) {
		HttpRequest request = new HttpRequest();
		request.setListener(listener);
		request.setType(HttpRequest.Type.LoginWithSocialId);
		request.loginWithSocialId(sn, snid);
	}

	public static void findAllUsers(ResponceListener listener) {
		HttpRequest request = new HttpRequest();
		request.setListener(listener);
		request.setType(HttpRequest.Type.FindAllUsers);
		request.findAllUsers();
	}

	public static void createEvent(ResponceListener listener, String mySn,
			String mySnId, String targetSn, String targetSnId, String feeling,
			String rant) {
		HttpRequest request = new HttpRequest();
		request.setListener(listener);
		request.setType(HttpRequest.Type.CreateEvent);
		request.createEvent(mySn, mySnId, targetSn, targetSnId, feeling, rant);
	}

}
