package com.tweetsreader.tasks;

import com.tweetsreader.main.IAuthenticationCallback;
import com.tweetsreader.main.OAuthSingleton;
import com.tweetsreader.utils.Constants;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class RetrieveAccessToken extends AsyncTask<Object, Void, Void> {

	String TAG = "RetrieveRequestToken";
	OAuthProvider httpOauthprovider;
	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthSingleton singleton = OAuthSingleton.getInstance();
	Context context;
	private String userKey;
	private String userSecret;
	private IAuthenticationCallback listener;
	private boolean errorOccured;

	Uri uri;

	public RetrieveAccessToken(IAuthenticationCallback listener) {
		this.listener = listener;
	}

	@Override
	protected Void doInBackground(Object... params) {
		httpOauthprovider = singleton.httpOauthprovider;
		httpOauthConsumer = singleton.httpOauthConsumer;
		context = (Context) params[0];
		uri = (Uri) params[1];

		if (uri != null && uri.toString().startsWith(Constants.CALLBACKURL)) {

			String verifier = uri
					.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			try {
				// retrieve the access token from the consumer and the OAuth
				// verifier returner by the Twitter Callback URL
				httpOauthprovider.retrieveAccessToken(httpOauthConsumer,
						verifier);
				userKey = httpOauthConsumer.getToken();
				userSecret = httpOauthConsumer.getTokenSecret();
			} catch (Exception e) {
				e.printStackTrace();
				errorOccured = true;

			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (errorOccured) {
			Toast.makeText(context, "Authentication failure",
					Toast.LENGTH_SHORT).show();

		} else {
			// Save user_key and user_secret in user preferences and return
			SharedPreferences settings = context.getSharedPreferences(
					Constants.TWEET_READER_PREF, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.USER_KEY, userKey);
			editor.putString(Constants.USER_SECRET, userSecret);
			editor.commit();
			listener.onAuthenticationCompleted();
		}
	}
}
