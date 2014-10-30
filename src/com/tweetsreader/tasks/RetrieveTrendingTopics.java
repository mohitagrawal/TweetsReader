package com.tweetsreader.tasks;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tweetsreader.main.IAuthenticationCallback;
import com.tweetsreader.main.ITrendingTopicsCallback;
import com.tweetsreader.main.OAuthSingleton;
import com.tweetsreader.utils.Constants;

import android.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class RetrieveTrendingTopics extends AsyncTask<Object, Void, Void> {

	Context context;
	OAuthSingleton singleton = OAuthSingleton.getInstance();
	private ITrendingTopicsCallback listener;	
	private boolean errorOccurred;

	public RetrieveTrendingTopics(ITrendingTopicsCallback listener) {
		this.listener = listener;
	}
	
	protected Void doInBackground(Object... paras) {
		context = (Context) paras[0];
		
		HttpGet get = new HttpGet(
				"https://api.twitter.com/1.1/trends/place.json?id=1");
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		get.setParams(params);

		try {
			SharedPreferences newsettings = context.getSharedPreferences(
					Constants.TWEET_READER_PREF, 0);
			String userKey = newsettings.getString(Constants.USER_KEY, "");
			String userSecret = newsettings
					.getString(Constants.USER_SECRET, "");

			singleton.httpOauthConsumer.setTokenWithSecret(userKey, userSecret);
			singleton.httpOauthConsumer.sign(get);

			DefaultHttpClient client = new DefaultHttpClient();
			String responsex = client.execute(get, new BasicResponseHandler());
			JSONArray array = new JSONArray(responsex);
			JSONObject trendsObject = array.getJSONObject(0);
			String asOf = trendsObject.getString("as_of");
			JSONArray trendsArray = trendsObject.getJSONArray("trends");
			StringBuilder sb = new StringBuilder();
			ArrayList<String> topics = new ArrayList<String>();
			for (int i = 0; i < trendsArray.length(); i++) {
				JSONObject trend = trendsArray.getJSONObject(i);
			
				sb.append("name: " + trend.getString("name") + "\n");
				topics.add((String)trend.get("name"));				
			}
			String[] topicsArray = new String[topics.size()];
			for(int i = 0;i <topics.size();i++){
				topicsArray[i]=topics.get(i);
			}
			listener.onTrendingTopicsFetched(topicsArray);
		} catch (Exception e) {
			// handle this somehow
			e.printStackTrace();	
			errorOccurred=true;
		}
		return null;
	}

	protected void onPostExecute(Void result) {	
		if (errorOccurred) {
			Toast.makeText(context, "Communication failure",
					Toast.LENGTH_SHORT).show();

		}
		//listener
	}

}
