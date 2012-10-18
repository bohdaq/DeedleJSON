package com.appspot.deedleit.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class Deedle implements EntryPoint {
	//<script type="text/javascript" language="javascript" src="deedle/deedle.nocache.js"></script>

	//JSON RPC
	private static final String JSON_TIMELINE = "{ \"email\": \"example@gmail.com\", \"count\": 3, \"skip\": 20, \"rating\": \"all\", \"type\": \"all\", \"locations\": { \"city\": \"abercrombie\", \"country\": \"Ukraine\" } }";
	private static final String JSON_URL = GWT.getHostPageBaseURL()
			+ "api/v2/timeline?json=" + JSON_TIMELINE;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {

		RootPanel.get("timeline").setVisible(false);
		final Hyperlink showTimeline = new Hyperlink();
		showTimeline.setText("Show Timeline");
		showTimeline.setStyleName("login");
		showTimeline.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(Widget sender) {
				if (showTimeline.getText().equals("Show Timeline")) {
					RootPanel.get("slider_wrapper").setVisible(false);
					RootPanel.get("home-1").setVisible(false);
					RootPanel.get("home-2").setVisible(false);
					RootPanel.get("timeline").setVisible(true);
					showTimeline.setText("Home");
					//Window.alert("click");
				} else if(showTimeline.getText().equals("Home")){
					RootPanel.get("timeline").setVisible(false);
					RootPanel.get("slider_wrapper").setVisible(true);
					RootPanel.get("home-1").setVisible(true);
					RootPanel.get("home-2").setVisible(true);
					showTimeline.setText("Show Timeline");
				}
			}
		});
		RootPanel.get("switchLink").add(showTimeline);
		
		
		//Getting JSON data:
		getJSONDataFromTimeline();
		
	
		
	}
	private void getJSONDataFromTimeline() {
		Window.alert("Hello world!" + JSON_URL);
		
		String url = JSON_URL;
		
		// Send request to server and catch any errors.
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("Couldn't retrieve JSON");
				}
				
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						Window.alert(asArrayOfStockData(response.getText()).get(0).getEmail());
					} else {
						Window.alert("Couldn't retrieve JSON (" + response.getStatusText()
								+ ")");
					}
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Couldn't retrieve JSON");
		}
		
	}

	private final native JsArray<TimelineData> asArrayOfStockData(String json) /*-{
	    return eval(json);
	  }-*/;

}
