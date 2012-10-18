package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.LikeEntity;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Dislike extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		LikeEntity likeJsonObj = gson.fromJson(json, LikeEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String authorEmail = "bob";

		Key itemKey = KeyFactory.createKey("item", likeJsonObj.getPhotoId());
		try {
			Entity item = ds.get(itemKey);
			authorEmail = (String) item.getProperty("email");
			Long totallike = (Long) item.getProperty("like");
			Long sumlike = totallike - 1;
			item.setProperty("like", sumlike);
			ds.put(item);
		} catch (EntityNotFoundException e) {
			out.println(JSONUtil.fail());
		}

		Key authorKey = KeyFactory.createKey("author", authorEmail);
		try {
			Entity author = ds.get(authorKey);
			Long totallike = (Long) author.getProperty("like");
			Long sumlike = totallike - 1;
			author.setProperty("like", sumlike);
			// setting rated items
			if (author.getProperty("likedItems").toString().isEmpty()) {
				author.setProperty("likedItems", likeJsonObj.getPhotoId());
			}
			ds.put(author);
		} catch (EntityNotFoundException e) {
			out.println(JSONUtil.fail());
		}

		out.print(JSONUtil.success());
	}
}
