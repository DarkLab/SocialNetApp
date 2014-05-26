package com.darklab.socialnetapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Formatter;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

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
		FindAllUsers,
		FindEvents
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
	public static final String FIND_EVENTS = BASE_URL + "findEvents";
	
	private Context mAppContext;
	private Formatter mFormatter;
	
	public HttpRequest(Context c){
		mAppContext = c;
		mFormatter = new Formatter(Locale.US);
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
			String targetSn, String targetSnId, String feeling, String rant) {
		createEvent(mySn, mySnId, targetSn, targetSnId, feeling, rant, null, null);
	}
	
	public void createEvent(String mySn, String mySnId,
			String targetSn, String targetSnId, String feeling, Double lat, Double lon) {
		createEvent(mySn, mySnId, targetSn, targetSnId, feeling, null, lat, lon);
	}

	public void createEvent(String mySn, String mySnId,
			String targetSn, String targetSnId, String feeling, String rant, Double lat, Double lon) {
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
			String str = mFormatter.format("%f", lat).toString();
			builder.appendQueryParameter("lat", mFormatter.format("%f", lat).toString())
			.appendQueryParameter("lon", mFormatter.format("%f", lon).toString());
		}
		String url = builder.build().toString();
		new RequestMakerTask().execute(url, METHOD_POST);
	}
	
	public void findEvents(Double lat, Double lon){
		findEvents(lat, lon, null);
	}
	
	public void findEvents(Double lat, Double lon, Double dist){
//		HttpClient client = new DefaultHttpClient();
//		HttpParams params = new BasicHttpParams();
//		params.setDoubleParameter("lat", lat);
//		params.setDoubleParameter("lon", lon);
//		params.setDoubleParameter("dist", dist);
//		HttpPost post = new HttpPost(FIND_EVENTS);
//		post.setParams(params);
//		try {
//			HttpResponse responsePost = client.execute(post);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Builder builder = Uri.parse(FIND_EVENTS).buildUpon()
				.appendQueryParameter("lat", String.valueOf(lat))
				.appendQueryParameter("lon", String.valueOf(lon));
		if (dist!= null) {
			builder.appendQueryParameter("dist", String.valueOf(dist));
		}
		String url = builder.build().toString();
		new RequestMakerTask().execute(url, METHOD_GET);
	}
	
	public void onPostRequest(String responce){
		String data = null;
		if (mListener != null) {
			data = responce;
		}else{
			data = "{\"error\":{\"message\":\"No listener\"}}";
		}
		mListener.onResponseReceived(data, this);
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
