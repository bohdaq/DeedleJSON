package com.appspot.deedleit.server.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

	public static String getTimeline(String email, Integer count) {
		// "timeline": - JSONArray of Items from datastore,
		// where comments - Map, attached to "comments"
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		List<String> followingList = new ArrayList<String>();
		
		Key authorKey = KeyFactory.createKey("author", email);
		try {
			Entity author = ds.get(authorKey);
			String following = (String) author.getProperty("following");
			followingList = Arrays.asList(following.split("---"));

		} catch (EntityNotFoundException e2) {
			e2.printStackTrace();
		}		
		
		Query query = new Query("item");
		
		
		List<Filter> resultFilterList = new ArrayList<Filter>();
		
//		if (type.equals("all")) {
//			resultFilterList.add(new FilterPredicate("email",
//					Query.FilterOperator.EQUAL, email));
//		} else if (type.equals("following")){
//			CompositeFilter followingFilter = onlyFollowing(followingList);
//			resultFilterList.addAll(followingFilter.getSubFilters());
//		}
		
//		if (city != null || !(city.equals("")) ){
//			resultFilterList.add(new FilterPredicate("city",
//					Query.FilterOperator.EQUAL, city));
//		}
//		if (country != null || !(country.equals("")) ){
//			resultFilterList.add(new FilterPredicate("country",
//					Query.FilterOperator.EQUAL, country));
//		}

		CompositeFilter resultFilter = new CompositeFilter(CompositeFilterOperator.AND, resultFilterList);
		query.setFilter(resultFilter);
		List<Entity> allEntities = ds.prepare(query).asList(
				FetchOptions.Builder.withLimit(100));
		Collections.sort(allEntities, new DateComparator());

//		allEntities = selectItemsByRating(allEntities, rating);
		
//		if (skip != null || skip != 0) {
//			int listSize = allEntities.size();
//			allEntities.subList(skip, listSize);
//		}
		
		
		allEntities.subList(0, count-1);
		
		
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
				Query commentQuery = new Query("comments");
				commentQuery.setFilter(new FilterPredicate("photoId",
						Query.FilterOperator.EQUAL, e.getProperty("photoId")));
				List<Entity> commentsEntities = ds.prepare(commentQuery).asList(
						FetchOptions.Builder.withLimit(count));
				for (Entity comment : commentsEntities){
					Map<String, String> commentMap = new HashMap<String, String>();
					commentMap.put("email", (String) comment.getProperty("email"));
					commentMap.put("photoId", (String) comment.getProperty("photoId"));
					commentMap.put("comment", (String) comment.getProperty("comment"));
					JSONObject singleCommentJsonObject = new JSONObject(commentMap);
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
	
	private static CompositeFilter onlyFollowing(List<String> followingList) {
		List<Filter> filtersList = new ArrayList<Filter>();
		
		for(String followingEmail : followingList){
			filtersList.add(new FilterPredicate("email",
					Query.FilterOperator.EQUAL, followingEmail));
		}
		
		CompositeFilter compositeFollowingFilter = new Query.CompositeFilter(CompositeFilterOperator.OR, filtersList);
		
		return compositeFollowingFilter;
	}

	private static List<Entity> selectItemsByRating(List<Entity> allEntities, String rating) {
		if (rating == null){
			return allEntities;
		}
		if (rating.equals("all")){
			return allEntities;
		}
		if (rating.equals("like")){
			for(Entity e : allEntities){
				Long first =(Long) e.getProperty("like");
				Long second =(Long) e.getProperty("unlike");
				if (second-first>0){
					allEntities.remove(e);
				}
			}
		} else {
			for(Entity e : allEntities){
				Long first =(Long) e.getProperty("like");
				Long second =(Long) e.getProperty("unlike");
				if (first-second>0){
					allEntities.remove(e);
				}
			}
		}
		return allEntities;
	}
}
