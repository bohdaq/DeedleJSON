package com.appspot.deedleit.client;

import com.google.gwt.core.client.EntryPoint;


public class Deedle implements EntryPoint {

//	private static final String JSON_TIMELINE = "{ \"email\": \"example@gmail.com\", \"count\": 3, \"skip\": 20, \"rating\": \"all\", \"type\": \"all\", \"locations\": { \"city\": \"abercrombie\", \"country\": \"Ukraine\" } }";
//	private static final String JSON_URL = GWT.getHostPageBaseURL()
//			+ "api/v2/timeline?json=" + JSON_TIMELINE;

	@Override
	public void onModuleLoad() {
		
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
