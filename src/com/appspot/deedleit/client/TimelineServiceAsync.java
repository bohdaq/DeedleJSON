package com.appspot.deedleit.client;

import java.util.ArrayList;

import com.appspot.deedleit.server.TimelineServiceEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;


public interface TimelineServiceAsync {
	void getTimeline(String email, Integer count, Integer skip, String rating, String type, String city, String country, AsyncCallback<ArrayList<TimelineServiceEntity>> callback)
			throws IllegalArgumentException;
}
