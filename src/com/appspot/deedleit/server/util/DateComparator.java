package com.appspot.deedleit.server.util;

import java.util.Comparator;

import com.google.appengine.api.datastore.Entity;

public class DateComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity e1, Entity e2) {
		Long first = (Long) e1.getProperty("date");
		Long second = (Long) e2.getProperty("date");
		return (int) (second - first);
	}
}
