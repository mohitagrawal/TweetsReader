package com.tweetsreader.main;

import com.example.tweetsreader.R;
import com.tweetsreader.utils.Constants;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Demonstration of using ListFragment to show a list of items from a canned
 * array.
 */
public class TweetsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.nothing, R.anim.nothing);

		String[] topics = getIntent().getStringArrayExtra(
				Constants.TRENDING_TOPICS);
		
		setContentView(R.layout.list);
		FragmentManager fm = getSupportFragmentManager();
		ShowTopicsListFragment list = new ShowTopicsListFragment();
		Bundle args = new Bundle();
		args.putStringArray(Constants.TRENDING_TOPICS, topics);
		list.setArguments(args);

		android.support.v4.app.FragmentTransaction fragmentTransaction = fm
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,
				R.anim.nothing, R.anim.nothing, R.anim.slide_out_right);

		fragmentTransaction.replace(android.R.id.content, list);
		fragmentTransaction.commit();

	}
}