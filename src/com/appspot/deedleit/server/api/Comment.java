package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.CommentEntity;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Comment extends HttpServlet {

	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		CommentEntity commentJsonObj = gson.fromJson(json, CommentEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Key authorKey = KeyFactory.createKey("item",
				commentJsonObj.getPhotoId());
		try {
			Entity author = ds.get(authorKey);
			if (author.getProperty("comments").toString().isEmpty()) {
				author.setProperty("comments",
						getSingleComment(commentJsonObj));
			} else {
				String followingTotalString = (String) author
						.getProperty("comments");
				StringBuilder likedItems = new StringBuilder(
						followingTotalString);
				likedItems.append("---");
				likedItems.append(getSingleComment(commentJsonObj));
				author.setProperty("comments", likedItems.toString());
			}
			ds.put(author);
			out.println(JSONUtil.success());
		} catch (EntityNotFoundException e) {
			out.println(JSONUtil.fail());
		}
	}
	private String getSingleComment(CommentEntity comentEntity) {
		StringBuilder comment = new StringBuilder();
		comment.append(comentEntity.getEmail());
		comment.append(SEPARATOR);
		comment.append(comentEntity.getPhotoId());
		comment.append(SEPARATOR);
		comment.append(comentEntity.getComment());
		return comment.toString();
	}
	private static final String SEPARATOR = "#$*";
}
