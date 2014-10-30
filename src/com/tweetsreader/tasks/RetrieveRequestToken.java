package com.tweetsreader.tasks;

import com.tweetsreader.main.OAuthSingleton;
import com.tweetsreader.utils.Constants;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class RetrieveRequestToken extends AsyncTask<Object , Void , Void> {
	
	private String TAG = "RetrieveRequestToken";
	OAuthProvider httpOauthprovider;
	CommonsHttpOAuthConsumer httpOauthConsumer;
	Context context;
	private String authUrl;
	OAuthSingleton singleton = OAuthSingleton.getInstance();
	private boolean errorOccurred;
	
	protected Void  doInBackground(Object... params) {
		httpOauthprovider = singleton.httpOauthprovider;
		httpOauthConsumer = singleton.httpOauthConsumer;
		context=(Context) params[0];
			try {
				authUrl =  httpOauthprovider.retrieveRequestToken(httpOauthConsumer, Constants.CALLBACKURL);
			} catch (Exception e) {			
				//e.printStackTrace();
			errorOccurred=true;				
			}		
		return null;
	}

	protected void onPostExecute(Void result) {
		if (errorOccurred) {
			Toast.makeText(context, "Authentication failure",
					Toast.LENGTH_SHORT).show();
		} 
		
		if (authUrl != null && authUrl.length()!= 0) {
			Log.d(TAG, "Twitter OAuth URL: " + authUrl);
			// launching the browser
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.context.startActivity(intent);
		}
	}

}
