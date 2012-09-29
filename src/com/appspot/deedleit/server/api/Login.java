package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.Author;
import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
@SuppressWarnings("serial")
public class Login extends HttpServlet {

	private Gson gson = new Gson();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("email");
		PrintWriter out = resp.getWriter();
		out.print(JSONUtil.getUserInfoData(email));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		Author author = gson.fromJson(json, Author.class);

//		 out.println(author.getName());
//		 out.println(author.getEmail());
//		 out.println(author.getPhotoId());
//		 out.println(author.getFolderId());

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key authorKey = KeyFactory.createKey("author", author.getEmail());
		Entity authorEntity = new Entity(authorKey);
		authorEntity.setProperty("email", author.getEmail());
		authorEntity.setProperty("name", author.getName());
		authorEntity.setProperty("photoId", author.getPhotoId());
		authorEntity.setProperty("folderId", author.getFolderId());
		authorEntity.setProperty("like", Long.valueOf(0));
		authorEntity.setProperty("unlike", Long.valueOf(0));
		authorEntity.setProperty("likedItems", "");
		authorEntity.setProperty("following", "");
		ds.put(authorEntity);

		out.print(JSONUtil.success());
	}

}
