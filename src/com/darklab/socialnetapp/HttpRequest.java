package com.darklab.socialnetapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class HttpRequest {
	public enum Type{
		LoginWithSocialId,
		CreateEvent,
		FindAllUsers
	}
	
	public static final String TAG = "HttpRequest";
	
	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	
	private static final String LOCALHOST = "http://192.168.0.99:9000/";
	private static final String BASE_URL = LOCALHOST;

	public static final String TEST0 = BASE_URL + "test0";
	public static final String LOGIN_WITH_SOCIAL_ID = BASE_URL + "loginWithSocialId";
	public static final String FIND_ALL_USERS = BASE_URL + "findAllUsers";
	public static final String CREATE_EVENT = BASE_URL + "createEvent";
	
	private Context mAppContext;
	
	public HttpRequest(Context c){
		mAppContext = c;
	}
	
	ResponceListener mListener;
	Type type;

	public ResponceListener getListener() {
		return mListener;
	}

	public void setListener(ResponceListener listener) {
		mListener = listener;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void loginWithSocialId(String sn, String snid){
		String url = Uri.parse(LOGIN_WITH_SOCIAL_ID)
				.buildUpon().appendQueryParameter("sn", sn)
				.appendQueryParameter("snid", snid).build().toString();
		new RequestMakerTask().execute(url, METHOD_POST);
	}
	
	public void findAllUsers() {
		findAllUsers(null, null);
	}

	public void findAllUsers(String start, String pageSize) {
		Builder builder = Uri.parse(FIND_ALL_USERS)
				.buildUpon();
		if (start != null) {
			builder.appendQueryParameter("start", start);
		}
		if (pageSize != null) {
			builder.appendQueryParameter("pageSize", pageSize);
		}
		String url = builder.build().toString();
		new RequestMakerTask().execute(url, METHOD_POST);
	}

	public void createEvent(String mySn, String mySnId,
			String targetSn, String targetSnId, String feeling) {
		createEvent(mySn, mySnId, targetSn, targetSnId, feeling, null, null, null);
	}

	public void createEvent(String mySn, String mySnId,
			String targetSn, String targetSnId, String feeling, String rant, String lat, String lon) {
		Builder builder = Uri.parse(CREATE_EVENT).buildUpon()
				.appendQueryParameter("mySn", mySn)
				.appendQueryParameter("mySnId", mySnId)
				.appendQueryParameter("targetSn", targetSn)
				.appendQueryParameter("targetSnId", targetSnId)
				.appendQueryParameter("feeling", feeling);
		if (rant != null) {
			builder.appendQueryParameter("rant", rant);
		}
		if (lat != null && lon != null) {
			builder.appendQueryParameter("lat", lat)
			.appendQueryParameter("lon", lon);
		}
		String url = builder.build().toString();
		new RequestMakerTask().execute(url, METHOD_POST);
	}
	
	public void onPostRequest(String responce){
		String data = null;
		if (mListener != null) {
			data = responce;
		}else{
			data = "{\"error\":{\"message\":\"No listener\"}}";
		}
		mListener.onResponceReceived(data, this);
	}

	public class RequestMakerTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String jsonString = null;
			try {
				jsonString = getResultString(params[0], params[1]);
			} catch (Exception e) {
				Log.e(TAG, "Error: ", e);
			}
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			onPostRequest(result);
		}
		
		public String getResultString(String urlSpec, String method) {
			StringBuilder builder = new StringBuilder();
			HttpURLConnection connection = null;
			try {
				URL url = new URL(urlSpec);
				connection = (HttpURLConnection) url.openConnection();
				
				connection.setReadTimeout(1000 * 10);
				connection.setConnectTimeout(1000 * 15);
				// connection.setDoInput(true);
				connection.setRequestMethod(method);

				String userpassword = getUserUUID() + ":" + getUserPassword();
				if (!userpassword.equals(":")) {
					byte[] data = userpassword.getBytes("UTF-8");
					String encodedAuthorization = Base64.encodeToString(data,
							Base64.DEFAULT);
					connection.setRequestProperty("Authorization", "Basic "
							+ encodedAuthorization);
				}

				connection.setRequestProperty("APPLICATION_TOKEN", "1234567");
				
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				String line = "";
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} catch (Exception e) {
				return e.getMessage();
			}finally{
				if (connection != null) {
					connection.disconnect();
				}
			}

			return builder.toString();

		}

		private String getUserPassword() {
			return AppVariables.get(mAppContext).getPassword();
		}

		private String getUserUUID() {
			return AppVariables.get(mAppContext).getUserId();
		}

	}

}
