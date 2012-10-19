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
public class Unfollow extends HttpServlet {

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
			if (!(author.getProperty("following").toString().isEmpty())) {
				String followingTotalString = (String) author.getProperty("following");
				String likedFirstStepOfRemove = followingTotalString.replace(followJsonObj.getEmailToFollow(), "");
				String likedResult = likedFirstStepOfRemove.replace("------", "---");
				author.setProperty("following", likedResult);
			}
			ds.put(author);
			out.println(JSONUtil.success());
		} catch (EntityNotFoundException e) {
			out.println(JSONUtil.fail());
		}
	}

}
