package com.appspot.deedleit.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.appspot.deedleit.client.TimelineService;
import com.appspot.deedleit.server.json.JSONUtil;
import com.appspot.deedleit.shared.TimelineServiceEntity;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TimelineServiceImpl extends RemoteServiceServlet implements
		TimelineService {

	private static final Logger log = Logger.getLogger(TimelineServiceImpl.class.getName()); 
	
	public ArrayList<TimelineServiceEntity> getTimeline(String email,
			Integer count, Integer skip, String rating, String type,
			String city, String country) throws IllegalArgumentException {
		
		
		log.info("getTimeline()");
		String jArrayString = JSONUtil.getTimeline(email, count, skip, rating,
				type, city, country);
		
		ArrayList<TimelineServiceEntity> resultList = new ArrayList<TimelineServiceEntity>();
		
		try {
			JSONArray jArray = new JSONArray(jArrayString);
			for (int i = 0; i < jArray.length(); i++) {

				JSONObject jObject = jArray.getJSONObject(i);

				String title = jObject.getString("title");
				String emailNeeded = jObject.getString("email");
				String description = jObject.getString("description");
				String photoUrl = fetchDownloadUrl(jObject.getString("photoId"));
				String cityNeeded = jObject.getString("city");
				Long date = jObject.getLong("date");
				Double latitude = jObject.getDouble("latitude");
				Double longtitude = jObject.getDouble("longtitude");
				Integer like = jObject.getInt("like");
				Integer unlike = jObject.getInt("unlike");

				resultList.add(new TimelineServiceEntity(title, emailNeeded,
						description, photoUrl, cityNeeded, date, latitude,
						longtitude, like, unlike));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	private String fetchDownloadUrl(String photoId) {
		// TODO impl URLFETCH for download url. See this page:
		// https://developers.google.com/drive/v2/reference/files/get
		
		
		URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();

		String response = "default response fetchDownloadUrl";
		try {
			URL url = new URL(URL_DRIVE_PREFIX + photoId + URL_DOWNLOAD_SUFFIX
					+ URL_API_KEY);

			HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);

			HTTPResponse httpResponse = urlFetchService.fetch(request);
	      response = new String(httpResponse.getContent());
		} catch (MalformedURLException e) {
			e.getStackTrace();
		} catch (IOException e) {
			e.getStackTrace();
		}

		String photoUrl = parseJSONObjectForDownloadUrl(response);

		return photoUrl;
	}
	private String parseJSONObjectForDownloadUrl(String response) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(response);
			return jObject.getString("thumbnailLink");
		} catch (JSONException e) {
			return e.getLocalizedMessage();
	}
		
	}
	private static final String URL_DRIVE_PREFIX = "https://www.googleapis.com/drive/v2/files/";
	private static final String URL_DOWNLOAD_SUFFIX = "?fields=thumbnailLink";
	private static final String URL_API_KEY = "&key=AIzaSyAjfaP-rN1Z3X-EcNmycG7POTQpDkWj4Q8";

}
