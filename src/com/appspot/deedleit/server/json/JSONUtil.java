package com.appspot.deedleit.server.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.FilterPredicate;

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
			return JSONUtil.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			return JSONUtil.fail();
		}
	}

	public static String getTimeline(String email, Integer count) {
		// "timeline": - JSONArray of Items from datastore,
		// where comments - Map, attached to "comments"
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("item");
		if (email != null) {
			query.setFilter(new FilterPredicate("email",
					Query.FilterOperator.EQUAL, email));
		}
		List<Entity> allEntities = ds.prepare(query).asList(
				FetchOptions.Builder.withLimit(count));
		
		JSONArray jArray = new JSONArray(); //result JSONArray
		for (Entity e : allEntities) {
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
				
				JSONArray jArrayForAttachingToComments = new JSONArray();
				//spliting to separate comment objects
				List<String> separateComments = Arrays.asList(e.getProperty("comments").toString().split("---"));
				for(String commentMap : separateComments){
					List<String> singleCommentProperty = Arrays.asList(commentMap.split("#$*"));
					
					Map<String, String> singleCommentMap = new HashMap<String, String>();
					singleCommentMap.put("email", singleCommentProperty.get(0));
					singleCommentMap.put("photoId", singleCommentProperty.get(1));
					singleCommentMap.put("comment", singleCommentProperty.get(2));
					JSONObject singleCommentJsonObject = new JSONObject(singleCommentMap);
					jArrayForAttachingToComments.put(singleCommentJsonObject);
				}
				jObject.put("comments", jArrayForAttachingToComments);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			jArray.put(jObject); //add object to result array - no deal to comments
		}
		return jArray.toString();
	}
}
