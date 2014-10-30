package com.tweetsreader.tasks;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tweetsreader.main.ISearchTweetsCallback;
import com.tweetsreader.main.OAuthSingleton;
import com.tweetsreader.utils.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class RetrieveTweets extends AsyncTask<Object, Void, Void> {

	Context context;
	OAuthSingleton singleton = OAuthSingleton.getInstance();
	private String searchTerm;
	private String[] tweetsArray;
	private boolean errorOccurred;
	private ISearchTweetsCallback listener;

	public RetrieveTweets(ISearchTweetsCallback listener) {
		this.listener = listener;
	}

	protected Void doInBackground(Object... paras) {
		context = (Context) paras[0];
		searchTerm = (String) paras[1];
		try {
			String query = URLEncoder.encode(searchTerm, "utf-8");
			HttpGet get = new HttpGet(
					"https://api.twitter.com/1.1/search/tweets.json?q=" + query
							+ "&count=10");
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setUseExpectContinue(params, false);
			get.setParams(params);

			SharedPreferences newsettings = context.getSharedPreferences(
					Constants.TWEET_READER_PREF, 0);
			String userKey = newsettings.getString(Constants.USER_KEY, "");
			String userSecret = newsettings
					.getString(Constants.USER_SECRET, "");

			singleton.httpOauthConsumer.setTokenWithSecret(userKey, userSecret);
			singleton.httpOauthConsumer.sign(get);

			DefaultHttpClient client = new DefaultHttpClient();
			String responsex = client.execute(get, new BasicResponseHandler());
			JSONObject array = new JSONObject(responsex);
			JSONArray statusesArray = array.getJSONArray("statuses");
			ArrayList<String> tweet = new ArrayList<String>();
			for (int i = 0; i < statusesArray.length(); i++) {
				JSONObject t = statusesArray.getJSONObject(i);
				tweet.add(t.getString("text"));
			}
			tweetsArray = new String[tweet.size()];
			for (int i = 0; i < tweet.size(); i++) {
				tweetsArray[i] = tweet.get(i);
			}
		} catch (Exception e) {
			// handle this somehow
			e.printStackTrace();
			errorOccurred=true;
		}
		return null;
	}

	protected void onPostExecute(Void result) {

		if (errorOccurred) {
			Toast.makeText(context, "Communication failure", Toast.LENGTH_SHORT)
					.show();

		} else {
			listener.onTrendingTweetsFetched(tweetsArray, searchTerm);
		}
	}

}
