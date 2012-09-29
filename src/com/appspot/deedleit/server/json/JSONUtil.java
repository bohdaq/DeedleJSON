package com.appspot.deedleit.server.json;

import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


public class JSONUtil {
	public static String success(){
		JSONObject jsonSuccess = new JSONObject();
		try {
			jsonSuccess.put("Status", new Integer(100));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSuccess.toString();
	}
	
	public static String fail(){
		JSONObject jsonSuccess = new JSONObject();
		try {
			jsonSuccess.put("Status", new Integer(101));
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
			json.put("email", author.getProperty("email"));
			json.put("name", author.getProperty("name"));
			json.put("photoId", author.getProperty("photoId"));
			json.put("folderId", author.getProperty("folderId"));
			json.put("unlike", author.getProperty("unlike"));
			
			String likedItems = (String) author.getProperty("likedItems");
			List<String> likedItemsList = Arrays.asList(likedItems.split("---"));
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
}
