package com.appspot.deedleit.client;

import com.appspot.deedleit.shared.Constants;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class Deedle implements EntryPoint {
	//<script type="text/javascript" language="javascript" src="deedle/deedle.nocache.js"></script>
	// https://developers.google.com/drive/v2/reference/files/get#try-it - whole scope
	// Cut s220, make full size!

	//JSON RPC
	private static final String JSON_TIMELINE = "{ \"email\": \"example@gmail.com\", \"count\": 10, \"skip\": 0, \"rating\": \"all\", \"type\": \"all\", \"locations\": { \"city\": null, \"country\": null } }";
	private static final String JSON_URL = GWT.getHostPageBaseURL()
			+ "api/v2/timelineClientSide?json=" + JSON_TIMELINE;
	
	
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
		JsArray<TimelineData> jsonTimelineArray = getJSONDataFromTimeline();
		// Getted jsonTimelineArray - parse in cycle (for) and set for each
		// element (.get(0)) its property (.get(...)) to div id="..."
		
//		//setting the main image
//		//HTML img = new HTML();
//		String imgUrl = jsonTimelineArray.get(0).getPhotoId();
//		//String imgRaw = "<img src=\"" + imgUrl + "\" class=\"img_round\" width=\"614\" height=\"314\" alt=\"\" />";
//		//img.setHTML(imgRaw);
//		Image img = new Image(imgUrl);
//		img.setStylePrimaryName("img_round");
//		RootPanel.get("timeline").add(img);
//		
//		Hyperlink title = new Hyperlink();
		HTML rawHtml = new HTML();
		rawHtml.setHTML(Constants.htmlTimelineItem);
		RootPanel.get("container_block").add(rawHtml);
		
		
		
		
	}
	private JsArray<TimelineData> getJSONDataFromTimeline() {
		
		final ResponseWrapper timelineArrayResponse = new ResponseWrapper();
		
		Window.alert(JSON_URL);
		
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
						timelineArrayResponse.setResponse(asArrayOfStockData(response.getText()));
						Window.alert(String.valueOf(asArrayOfStockData(response.getText()).length()));
						Window.alert(String.valueOf(asArrayOfStockData(response.getText()).get(0).getPhotoId()));
						Window.alert(String.valueOf(asArrayOfStockData(response.getText()).get(0).getUserPhotoId()));
						//String title = response.getText().get(0).getTitle();
						String total = Constants.preTitle + timelineArrayResponse.getResponse().get(0).getTitle() + Constants.postTitle;
						HTML rawHtml2 = new HTML();
						rawHtml2.setHTML(total);
						RootPanel.get("container_block").add(rawHtml2);
					} else {
						Window.alert("Couldn't retrieve JSON (" + response.getStatusText()
								+ ")");
					}
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Couldn't retrieve JSON");
		}
		return timelineArrayResponse.getResponse();		
	}
	class ResponseWrapper {
		JsArray<TimelineData> resp;
	    void setResponse(JsArray<TimelineData> resp) {
	        this.resp = resp;
	    }
	    JsArray<TimelineData> getResponse() {
	        return resp;
	    }
	}
	
	private final native JsArray<TimelineData> asArrayOfStockData(String json) /*-{
	    return eval(json);
	  }-*/;

}
