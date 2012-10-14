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

@SuppressWarnings("serial")
public class Timeline extends HttpServlet {

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String json = req.getParameter("json");
		try {
			JSONObject jsonParameter = new JSONObject(json);
			String email = jsonParameter.getString("email");
			Integer count = jsonParameter.getInt("count");
			Integer skip = jsonParameter.getInt("skip");
			String rating = jsonParameter.getString("rating");
			String type = jsonParameter.getString("type");
			JSONObject jObjectLocation = jsonParameter.getJSONObject("locations");
			String city = jObjectLocation.getString("city");
			String country = jObjectLocation.getString("country");
			
			
			String response = JSONUtil.getTimeline(email, count, skip, rating, type, city, country);
			
			out.print(response);
		} catch (JSONException e) {
			out.print(JSONUtil.fail());
			e.printStackTrace();
		}
	}
}
