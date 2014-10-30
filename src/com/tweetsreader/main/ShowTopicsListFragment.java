package com.tweetsreader.main;

import com.example.tweetsreader.R;
import com.tweetsreader.tasks.RetrieveTweets;
import com.tweetsreader.utils.Constants;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTopicsListFragment extends ListFragment implements
		ISearchTweetsCallback {

	private String[] topics;
	private ProgressDialog progressDialog;
	RetrieveTweets search;

	public ShowTopicsListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args.getStringArray(Constants.TRENDING_TOPICS) != null) {
			topics = args.getStringArray(Constants.TRENDING_TOPICS);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				topics);
		View view = inflater.inflate(R.layout.tweetlist, null);
		TextView header = (TextView) view.findViewById(R.id.listViewHeader);
		header.setText(getResources().getString(R.string.trending_topics));

		setListAdapter(adapter);
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String selection = l.getItemAtPosition(position).toString();
		progressDialog = ProgressDialog.show(getActivity(), "",
				"Retrieving tweets...");
		progressDialog.setCancelable(true);
		search = new RetrieveTweets(this);
		search.execute(getActivity(), selection);
	}

	@Override
	public void onTrendingTweetsFetched(String[] tweets, String topic) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		ShowTweetsListFragment list = new ShowTweetsListFragment();
		Bundle args = new Bundle();
		args.putStringArray(Constants.TRENDING_TWEETS, tweets);
		args.putString("Topic", topic);
		list.setArguments(args);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fm
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,
				R.anim.nothing, R.anim.nothing, R.anim.slide_out_right);
		fragmentTransaction.replace(android.R.id.content, list);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

	}
}