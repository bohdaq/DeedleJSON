package com.appspot.deedleit.client;

import java.util.ArrayList;

import com.appspot.deedleit.shared.TimelineServiceEntity;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class Deedle implements EntryPoint {

	
	private final TimelineServiceAsync timelineItems = GWT
			.create(TimelineService.class);

	
	public void onModuleLoad() {
		timelineItems.getTimeline("example@gmail.com", 10, 0, "all", null, null,
				null, new AsyncCallback<ArrayList<TimelineServiceEntity>>() {
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(
							ArrayList<TimelineServiceEntity> result) {
						
					}
				});

//		// Add a handler to send the name to the server
//		MyHandler handler = new MyHandler();
//		sendButton.addClickHandler(handler);
//		nameField.addKeyUpHandler(handler);
	}
}
