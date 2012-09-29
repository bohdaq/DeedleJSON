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
public class Like extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		LikeEntity likeJsonObj = gson.fromJson(json, LikeEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String authorEmail;

		Key itemKey = KeyFactory.createKey("item", likeJsonObj.getPhotoId());
		try {
			// Step 1: +1 to item
			Entity item = ds.get(itemKey);
			authorEmail = (String) item.getProperty("email");
			Long totallike = (Long) item.getProperty("like");
			Long sumlike = totallike + 1;
			item.setProperty("like", sumlike);
			ds.put(item);

			// Step 2: +1 to author, add likedItem property
			Key authorKey = KeyFactory.createKey("author", authorEmail);
			Entity author = ds.get(authorKey);
			Long totalauthorlike = (Long) author.getProperty("like");
			Long sumauthorlike;
			sumauthorlike = totalauthorlike + 1;
			author.setProperty("like", sumauthorlike);
			// setting rated items
			if (author.getProperty("likedItems").toString().isEmpty()) {
				author.setProperty("likedItems", likeJsonObj.getPhotoId());
			} else {
				String likedTotalString = (String) author
						.getProperty("likedItems");
				StringBuilder likedItems = new StringBuilder(likedTotalString);
				likedItems.append("---");
				likedItems.append(likeJsonObj.getPhotoId());
				author.setProperty("likedItems", likedItems.toString());
			}
			ds.put(author);
			out.print(JSONUtil.success());
		} catch (EntityNotFoundException e) {
			out.print(JSONUtil.fail());
			e.printStackTrace();
		}
	}
}
