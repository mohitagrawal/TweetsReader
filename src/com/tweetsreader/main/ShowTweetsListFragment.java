package com.tweetsreader.main;

import com.example.tweetsreader.R;
import com.tweetsreader.utils.Constants;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowTweetsListFragment extends ListFragment    {
		
	private String[] tweets;
	private String topic;
	
	public ShowTweetsListFragment(){		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();		
		if(args != null){
			tweets = args.getStringArray(Constants.TRENDING_TWEETS);
			topic = args.getString("Topic");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				tweets);
		setListAdapter(adapter);
		if(adapter.getCount()==0)
		{
			
		}
		View view = inflater.inflate(R.layout.tweetlist, null);
		TextView header = (TextView) view.findViewById(R.id.listViewHeader);		
		header.setText(getResources().getString(R.string.trending_tweets)+" on \""+topic+"\"");
        return view;
	}

}