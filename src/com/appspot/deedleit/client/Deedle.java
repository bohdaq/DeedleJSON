package com.appspot.deedleit.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class Deedle implements EntryPoint {


	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {

		final Hyperlink showTimeline = new Hyperlink();
		showTimeline.setText("Show Timeline");
		//showTimeline.setStyleName("signin");
		showTimeline.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(Widget sender) {
				showTimeline.setText("Home");
				//changeViewToTimeline();
			}
		});
		RootPanel.get("switchLink").add(showTimeline);
		
		//JSON RPC
//	private static final String JSON_TIMELINE = "{ \"email\": \"example@gmail.com\", \"count\": 3, \"skip\": 20, \"rating\": \"all\", \"type\": \"all\", \"locations\": { \"city\": \"abercrombie\", \"country\": \"Ukraine\" } }";
//	private static final String JSON_URL = GWT.getHostPageBaseURL()
//			+ "api/v2/timeline?json=" + JSON_TIMELINE;
		// TODO Auto-generated method stub
//		Window.alert("Hello world!" + JSON_URL);
//		
//		String url = JSON_URL;
//		
//	    // Send request to server and catch any errors.
//	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
//
//	    try {
//	      Request request = builder.sendRequest(null, new RequestCallback() {
//	        public void onError(Request request, Throwable exception) {
//	          Window.alert("Couldn't retrieve JSON");
//	        }
//
//	        public void onResponseReceived(Request request, Response response) {
//	          if (200 == response.getStatusCode()) {
//	        	  Window.alert(asArrayOfStockData(response.getText()).get(0).getEmail());
//	          } else {
//	        	  Window.alert("Couldn't retrieve JSON (" + response.getStatusText()
//	                + ")");
//	          }
//	        }
//
//	      });
//	    } catch (RequestException e) {
//	    	Window.alert("Couldn't retrieve JSON");
//	    }
//
	}
//	
//	  private final native JsArray<TimelineData> asArrayOfStockData(String json) /*-{
//	    return eval(json);
//	  }-*/;

}
