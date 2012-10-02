package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.entity.LoginEntity;
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
		LoginEntity authorJsonObj = gson.fromJson(json, LoginEntity.class);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key authorKey = KeyFactory.createKey("author", authorJsonObj.getEmail());
		Entity author = new Entity(authorKey);
		author.setProperty("email", authorJsonObj.getEmail());
		author.setProperty("name", authorJsonObj.getName());
		author.setProperty("photoId", authorJsonObj.getPhotoId());
		author.setProperty("folderId", authorJsonObj.getFolderId());
		author.setProperty("city", authorJsonObj.getCity());
		author.setProperty("country", authorJsonObj.getCountry());
		author.setProperty("like", Long.valueOf(0));
		author.setProperty("unlike", Long.valueOf(0));
		author.setProperty("likedItems", "");
		author.setProperty("following", "");
		ds.put(author);

		out.print(JSONUtil.success());
	}

}
