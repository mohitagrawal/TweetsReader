package com.tweetsreader.main;

import com.tweetsreader.utils.Constants;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class OAuthSingleton {
	
	public OAuthProvider httpOauthprovider;
	public CommonsHttpOAuthConsumer httpOauthConsumer;

	private static OAuthSingleton singleton = new OAuthSingleton();

	private OAuthSingleton() {
		httpOauthprovider = new DefaultOAuthProvider(
				"https://api.twitter.com/oauth/request_token",
				"https://api.twitter.com/oauth/access_token",
				"https://api.twitter.com/oauth/authorize");
		httpOauthConsumer = new CommonsHttpOAuthConsumer(Constants.consumerKey,
				Constants.consumerSecret);
	}

	public static OAuthSingleton getInstance() {
		return singleton;
	}	
}
