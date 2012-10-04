package com.appspot.deedleit.server;

import java.util.ArrayList;

import com.appspot.deedleit.client.TimelineService;
import com.appspot.deedleit.server.json.JSONUtil;
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

	public ArrayList<TimelineServiceEntity> getTimeline(String email,
			Integer count, Integer skip, String rating, String type,
			String city, String country) throws IllegalArgumentException {

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
				String photoUrl = fethcDownloadUrl(jObject.getString("photoId"));
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

	private String fethcDownloadUrl(String string) {
		// TODO impl URLFETCH for download url. See this page:
		// https://developers.google.com/drive/v2/reference/files/get
		return null;
	}

}
