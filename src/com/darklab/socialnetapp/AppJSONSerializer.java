package com.darklab.socialnetapp;

import org.json.JSONException;
import org.json.JSONObject;

public class AppJSONSerializer {
	
	private static final String JSON_USER_ID = "userId";

	public String getUserId(JSONObject json) throws JSONException{
		return json.getString(JSON_USER_ID);
	}

}
