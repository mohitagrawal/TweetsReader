package com.tweetsreader.main;

import com.example.tweetsreader.R;
import com.tweetsreader.tasks.RetrieveAccessToken;
import com.tweetsreader.tasks.RetrieveRequestToken;
import com.tweetsreader.tasks.RetrieveTrendingTopics;
import com.tweetsreader.utils.Constants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity implements
		IAuthenticationCallback, ITrendingTopicsCallback {

	OAuthSingleton singleton = OAuthSingleton.getInstance();
	private ProgressDialog progressDialog;
	RetrieveTrendingTopics fetchTopics;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button oauth = (Button) findViewById(R.id.oauth_button);

		this.overridePendingTransition(R.anim.slide_out_left,
				R.anim.slide_out_right);

		oauth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new RetrieveRequestToken().execute(getApplicationContext());

			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		progressDialog = ProgressDialog.show(this, "", "Retrieving topics...");
		progressDialog.setCancelable(true);
		RetrieveAccessToken access = new RetrieveAccessToken(this);
		access.execute(getApplicationContext(), uri);
	}

	public void onAuthenticationCompleted() {
		fetchTopics = new RetrieveTrendingTopics(this);
		fetchTopics.execute(getApplicationContext());
	}

	public void onBackPressed() {
		if (fetchTopics != null) {
			//Try to cancel task
			fetchTopics.cancel(true);
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
		return;
	}

	public void onTrendingTopicsFetched(String[] trendingTopics) {

		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		Intent intent = new Intent(getApplicationContext(),
				TweetsActivity.class);
		intent.putExtra(Constants.TRENDING_TOPICS, trendingTopics);
		startActivity(intent);
	}
}