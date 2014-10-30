package com.tweetsreader.main;

public interface ISearchTweetsCallback {
	void onTrendingTweetsFetched(String[] tweets, String topic);
}
