package com.appspot.deedleit.server.json;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import com.appspot.deedleit.server.util.DateComparator;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class JSONUtil {
	public static String success() {
		JSONObject jsonSuccess = new JSONObject();
		try {
			jsonSuccess.put("status", new Integer(100));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSuccess.toString();
	}

	public static String fail() {
		JSONObject jsonSuccess = new JSONObject();
		try {
			jsonSuccess.put("status", new Integer(101));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSuccess.toString();
	}

	public static String notRegistered() {
		JSONObject jsonSuccess = new JSONObject();
		try {
			jsonSuccess.put("status", new Integer(104));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSuccess.toString();
	}
	
	public static String getUserInfoData(String email) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key authorKey = KeyFactory.createKey("author", email);
		try {
			Entity author = ds.get(authorKey);
			JSONObject json = new JSONObject();
			json.put("status", new Integer(100));
			json.put("email", author.getProperty("email"));
			json.put("name", author.getProperty("name"));
			json.put("photoId", author.getProperty("photoId"));
			json.put("city", author.getProperty("city"));
			json.put("country", author.getProperty("country"));
			json.put("folderId", author.getProperty("folderId"));
			json.put("unlike", author.getProperty("unlike"));

			String likedItems = (String) author.getProperty("likedItems");
			List<String> likedItemsList = Arrays
					.asList(likedItems.split("---"));
			json.accumulate("likedItems", likedItemsList);

			String following = (String) author.getProperty("following");
			List<String> followingList = Arrays.asList(following.split("---"));
			json.accumulate("following", followingList);
			return json.toString();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return JSONUtil.notRegistered();
		} catch (JSONException e) {
			e.printStackTrace();
			return JSONUtil.fail();
		}
	}

	public static String getTimeline(String email, Integer count, Integer skip, String rating, String type, String city, String country) {
		// "timeline": - JSONArray of Items from datastore,
		// where comments - Map, attached to "comments"
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		
		// type: following
		List<String> followingList = new ArrayList<String>();
		
		Key authorKey = KeyFactory.createKey("author", email);
		try {
			Entity author = ds.get(authorKey);
			String following = (String) author.getProperty("following");
			followingList = Arrays.asList(following.split("---"));

		} catch (EntityNotFoundException e2) {
			e2.printStackTrace();
		}		
		
		
		// creating query
		Query query = new Query("item");
		
		// list of filters
		List<Filter> resultFilterList = new ArrayList<Filter>();

		// city
		if (city != null && !(city.equals("null"))) {
			resultFilterList.add(new FilterPredicate("city",
					Query.FilterOperator.EQUAL, city));
		}
		
		// country
		if (country != null && !(country.equals("null"))) {
			resultFilterList.add(new FilterPredicate("country",
					Query.FilterOperator.EQUAL, country));
		}
		
		// types If all - nothing to filter!
		if (type.equalsIgnoreCase("following")) {
			if (followingList.size() != 0 || followingList != null) {
				resultFilterList.add(onlyFollowing(followingList));
			}
		} else if (type.equalsIgnoreCase("mine")) {
			resultFilterList.add(new FilterPredicate("email", Query.FilterOperator.EQUAL, email));
		}
		
		
		if (resultFilterList.size() > 1) {
			CompositeFilter resultFilter = new CompositeFilter(CompositeFilterOperator.AND, resultFilterList);
			query.setFilter(resultFilter);
		} else if (resultFilterList.size() == 1) {
			query.setFilter(resultFilterList.get(0));
		}
		
		List<Entity> allEntities = ds.prepare(query).asList(
				FetchOptions.Builder.withLimit(100));
		Collections.sort(allEntities, new DateComparator());

		List<Entity> allEntitiesAfterRating = selectItemsByRating(allEntities, rating);
		
		JSONArray jArray = new JSONArray(); //result JSONArray
		
		
		for (int i = skip; i< allEntitiesAfterRating.size(); i++) {
			
			Entity e = allEntitiesAfterRating.get(i);
			JSONObject jObject = new JSONObject();
			try {
				jObject.put("title", e.getProperty("title"));
				jObject.put("email", e.getProperty("email"));
				jObject.put("description", e.getProperty("description"));
				jObject.put("photoId", e.getProperty("photoId"));
				jObject.put("date", e.getProperty("date"));
				jObject.put("latitude", e.getProperty("latitude"));
				jObject.put("longtitude", e.getProperty("longtitude"));
				jObject.put("like", e.getProperty("like"));
				jObject.put("unlike", e.getProperty("unlike"));
				jObject.put("city", e.getProperty("city"));
				jObject.put("country", e.getProperty("country"));
				jObject.put("userPhotoId", e.getProperty("userPhotoId"));
				jObject.put("name", e.getProperty("name"));
								
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			jArray.put(jObject); //add object to result array - no deal to comments
		}
		
		JSONArray resultJArray = new JSONArray();
		
		for(int i=0; i<jArray.length(); i++){
			if (i==count){
				break;
			}
			try {
				resultJArray.put(i, jArray.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resultJArray.toString();
	}
	
	public static String getUsersTimeline(String email, Integer count, Integer skip, String rating, String type, String city, String country) {
		// "timeline": - JSONArray of Items from datastore,
		// where comments - Map, attached to "comments"
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		
		// type: following
		List<String> followingList = new ArrayList<String>();
		
		Key authorKey = KeyFactory.createKey("author", email);
		try {
			Entity author = ds.get(authorKey);
			String following = (String) author.getProperty("following");
			followingList = Arrays.asList(following.split("---"));
			
		} catch (EntityNotFoundException e2) {
			e2.printStackTrace();
		}		
		
		
		// creating query
		Query query = new Query("author");
		
		// list of filters
		List<Filter> resultFilterList = new ArrayList<Filter>();
		
		// city
		if (city != null && !(city.equals("null"))) {
			resultFilterList.add(new FilterPredicate("city",
					Query.FilterOperator.EQUAL, city));
		}
		
		// country
		if (country != null && !(country.equals("null"))) {
			resultFilterList.add(new FilterPredicate("country",
					Query.FilterOperator.EQUAL, country));
		}
		
		// types If all - nothing to filter!
		if (type.equalsIgnoreCase("following")) {
			if (followingList.size() != 0 || followingList != null) {
				resultFilterList.add(onlyFollowing(followingList));
			}
		}
		
		if (resultFilterList.size() > 1) {
			CompositeFilter resultFilter = new CompositeFilter(CompositeFilterOperator.AND, resultFilterList);
			query.setFilter(resultFilter);
		} else if (resultFilterList.size() == 1) {
			query.setFilter(resultFilterList.get(0));
		}
		
		List<Entity> allEntities = ds.prepare(query).asList(
				FetchOptions.Builder.withLimit(100));
		//Collections.sort(allEntities, new DateComparator());
		
		List<Entity> allEntitiesAfterRating = selectItemsByRating(allEntities, rating);
		
		
		JSONArray jArray = new JSONArray(); //result JSONArray
		
		
		//TODO: Clear the shit that are getting from enteties!!!
		
		for (int i = skip; i< allEntitiesAfterRating.size(); i++) {
			
			Entity e = allEntitiesAfterRating.get(i);
			JSONObject jObject = new JSONObject();
			try {
				jObject.put("title", e.getProperty("title"));
				jObject.put("email", e.getProperty("email"));
				jObject.put("description", e.getProperty("description"));
				jObject.put("photoId", e.getProperty("photoId"));
				jObject.put("date", e.getProperty("date"));
				jObject.put("latitude", e.getProperty("latitude"));
				jObject.put("longtitude", e.getProperty("longtitude"));
				jObject.put("like", e.getProperty("like"));
				jObject.put("unlike", e.getProperty("unlike"));
				jObject.put("city", e.getProperty("city"));
				jObject.put("country", e.getProperty("country"));
				jObject.put("userPhotoId", e.getProperty("userPhotoId"));
				jObject.put("name", e.getProperty("name"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			jArray.put(jObject); //add object to result array - no deal to comments
		}
		
		JSONArray resultJArray = new JSONArray();
		
		for(int i=0; i<jArray.length(); i++){
			if (i==count){
				break;
			}
			try {
				resultJArray.put(i, jArray.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resultJArray.toString();
	}
	
	
	public static String getTimelineWithThumbs(String email, Integer count, Integer skip, String rating, String type, String city, String country) {
		// "timeline": - JSONArray of Items from datastore,
		// where comments - Map, attached to "comments"
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		
		// type: following
		List<String> followingList = new ArrayList<String>();
		
		Key authorKey = KeyFactory.createKey("author", email);
		try {
			Entity author = ds.get(authorKey);
			String following = (String) author.getProperty("following");
			followingList = Arrays.asList(following.split("---"));
			
		} catch (EntityNotFoundException e2) {
			e2.printStackTrace();
		}		
		
		
		// creating query
		Query query = new Query("item");
		
		// list of filters
		List<Filter> resultFilterList = new ArrayList<Filter>();
		
		// city
		if (city != null && !(city.equals("null"))) {
			resultFilterList.add(new FilterPredicate("city",
					Query.FilterOperator.EQUAL, city));
		}
		
		// country
		if (country != null && !(country.equals("null"))) {
			resultFilterList.add(new FilterPredicate("country",
					Query.FilterOperator.EQUAL, country));
		}
		
		// types If all - nothing to filter!
		if (type.equalsIgnoreCase("following")) {
			if (followingList.size() != 0 || followingList != null) {
				resultFilterList.add(onlyFollowing(followingList));
			}
		} else if (type.equalsIgnoreCase("mine")) {
			resultFilterList.add(new FilterPredicate("email", Query.FilterOperator.EQUAL, email));
		}
		
		
		if (resultFilterList.size() > 1) {
			CompositeFilter resultFilter = new CompositeFilter(CompositeFilterOperator.AND, resultFilterList);
			query.setFilter(resultFilter);
		} else if (resultFilterList.size() == 1) {
			query.setFilter(resultFilterList.get(0));
		}
		
		List<Entity> allEntities = ds.prepare(query).asList(
				FetchOptions.Builder.withLimit(100));
		Collections.sort(allEntities, new DateComparator());
		
		List<Entity> allEntitiesAfterRating = selectItemsByRating(allEntities, rating);
		
		JSONArray jArray = new JSONArray(); //result JSONArray
		
		
		for (int i = skip; i< allEntitiesAfterRating.size(); i++) {
			
			Entity e = allEntitiesAfterRating.get(i);
			JSONObject jObject = new JSONObject();
			try {
				jObject.put("title", e.getProperty("title"));
				jObject.put("email", e.getProperty("email"));
				jObject.put("description", e.getProperty("description"));
				jObject.put("photoId", getThumbUrl((String) e.getProperty("photoId")));
				jObject.put("date", e.getProperty("date"));
				jObject.put("latitude", e.getProperty("latitude"));
				jObject.put("longtitude", e.getProperty("longtitude"));
				jObject.put("like", e.getProperty("like"));
				jObject.put("unlike", e.getProperty("unlike"));
				jObject.put("city", e.getProperty("city"));
				jObject.put("country", e.getProperty("country"));
				jObject.put("userPhotoId", getThumbUrl((String) e.getProperty("userPhotoId")));
				jObject.put("name", e.getProperty("name"));
					
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			jArray.put(jObject); //add object to result array - no deal to comments
		}
		
		JSONArray resultJArray = new JSONArray();
		
		for(int i=0; i<jArray.length(); i++){
			if (i==count){
				break;
			}
			try {
				resultJArray.put(i, jArray.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resultJArray.toString();
	}
	
	private static String getThumbUrl(String photoId) {
		String result = "not working :(";

		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();

		try {
			String adress = "https://www.googleapis.com/drive/v2/files/"
					+ photoId + // fileId
					"?key=AIzaSyAjfaP-rN1Z3X-EcNmycG7POTQpDkWj4Q8";
			URL url = new URL(adress);
			HTTPResponse response = fetcher.fetch(url);
			byte[] content = response.getContent();
			String debug = new String(content);
			try {
				JSONObject json = new JSONObject(debug);
				String thumbnail = json.getString("thumbnailLink");
				result = thumbnail.replace("=s220", "=s1200");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated catch block

		} catch (IOException ex) {
			ex.getLocalizedMessage();
		}
		return result;

	}

	private static Filter onlyFollowing(List<String> followingList) {
		List<Filter> filtersList = new ArrayList<Filter>();
		
		for(String followingEmail : followingList){
			filtersList.add(new FilterPredicate("email",
					Query.FilterOperator.EQUAL, followingEmail));
		}
		Filter followingFilter;
		if (filtersList.size() > 1){
			followingFilter = CompositeFilterOperator.or(filtersList);
		} else {
			followingFilter  = filtersList.get(0);
		}
		
		return followingFilter;
	}

	private static List<Entity> selectItemsByRating(List<Entity> allEntities, String rating) {
		if (rating == null || rating.equals("") || rating.equalsIgnoreCase("all")){
			return allEntities;
		}
		
		List<Entity> ratedList = new ArrayList<Entity>();
		if (rating.equalsIgnoreCase("unlike")){
			for(Entity e : allEntities){
				Long first =(Long) e.getProperty("like");
				Long second =(Long) e.getProperty("unlike");
				if (second-first > 0){
					ratedList.add(e);
				}
			}
		} else if (rating.equalsIgnoreCase("like")) {
			for(Entity e : allEntities){
				Long first =(Long) e.getProperty("like");
				Long second =(Long) e.getProperty("unlike");
				if (first-second > 0){
					ratedList.add(e);
				}
			}
		}
		return ratedList;
	}
}
