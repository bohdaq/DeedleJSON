package com.appspot.deedleit.server.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.deedleit.server.json.JSONUtil;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class Timeline extends HttpServlet {

	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		try {
			JSONObject jsonParameter = new JSONObject(json);
			String email = jsonParameter.getString("email");
			Integer count = jsonParameter.getInt("count");
			String response = JSONUtil.getTimeline(email, count);
			
			out.print(response);
		} catch (JSONException e) {
			out.print(JSONUtil.fail());
			e.printStackTrace();
		}
	}
}
