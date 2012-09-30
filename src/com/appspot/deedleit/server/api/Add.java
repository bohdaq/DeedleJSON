package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.AddEntity;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Add extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		AddEntity addJsonObj = gson.fromJson(json, AddEntity.class);
		
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key itemKey = KeyFactory.createKey("item", addJsonObj.getPhotoId());
		Entity item = new Entity(itemKey);
		
		item.setProperty("email", addJsonObj.getEmail());
		item.setProperty("title", addJsonObj.getTitle());
		item.setProperty("photoId", addJsonObj.getPhotoId());
		item.setProperty("description", addJsonObj.getDescription());
		item.setProperty("date", addJsonObj.getDate());
		item.setProperty("latitude", addJsonObj.getLatitude());
		item.setProperty("longtitude", addJsonObj.getLongtitude());
		item.setProperty("like", Long.valueOf(0));
		item.setProperty("unlike", Long.valueOf(0));
		item.setProperty("comments", "");
		ds.put(item);
		out.print(JSONUtil.success());

	}

}
