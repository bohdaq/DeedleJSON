package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.CommentEntity;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Comment extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String json = req.getParameter("json");
		CommentEntity commentJsonObj = gson.fromJson(json, CommentEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		PrintWriter out = resp.getWriter();
		Entity comment = new Entity("comments");
		comment.setProperty("email", commentJsonObj.getEmail());
		comment.setProperty("deedId", commentJsonObj.getDeedId());
		comment.setProperty("comment", commentJsonObj.getComment());
		comment.setProperty("userPhotoId", commentJsonObj.getUserPhotoId());
		comment.setProperty("name", commentJsonObj.getName());
		comment.setProperty("date", commentJsonObj.getDate());
		ds.put(comment);
		out.println(JSONUtil.success());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String deedId = req.getParameter("deedId");
		PrintWriter out = resp.getWriter();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Filter deedFilter = new FilterPredicate("deedId", Query.FilterOperator.EQUAL, deedId);
		Query query = new Query("comments");
		query.setFilter(deedFilter);
		List<Entity> commentList = ds.prepare(query).asList(FetchOptions.Builder.withLimit(100));
		JSONArray jArray = new JSONArray();
		for (Entity e : commentList) {
			JSONObject commentJSON = new JSONObject();
			try {
				commentJSON.put("email", e.getProperty("email"));
				commentJSON.put("deedId", e.getProperty("deedId"));
				commentJSON.put("comment", e.getProperty("comment"));
				commentJSON.put("userPhotoId", e.getProperty("userPhotoId"));
				commentJSON.put("name", e.getProperty("name"));
				commentJSON.put("date", e.getProperty("date"));
			} catch (JSONException e1) {
				e1.getMessage();
			}
			jArray.put(commentJSON);
		}
		String result = jArray.toString();
		out.println(result);
	}

	//public static final String SEPARATOR = "#$*";
}
