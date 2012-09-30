package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.FollowEntity;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Follow extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		FollowEntity followJsonObj = gson.fromJson(json, FollowEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Key authorKey = KeyFactory
				.createKey("author", followJsonObj.getEmail());
		try {
			Entity author = ds.get(authorKey);
			if (author.getProperty("following").toString().isEmpty()) {
				author.setProperty("following",
						followJsonObj.getEmailToFollow());
			} else {
				String followingTotalString = (String) author
						.getProperty("following");
				StringBuilder likedItems = new StringBuilder(
						followingTotalString);
				likedItems.append("---");
				likedItems.append(followJsonObj.getEmailToFollow());
				author.setProperty("following", likedItems.toString());
			}
			ds.put(author);
			out.println(JSONUtil.success());
		} catch (EntityNotFoundException e) {
			out.println(JSONUtil.fail());
		}
	}

}
