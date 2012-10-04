package com.appspot.deedleit.client;

import java.util.ArrayList;

import com.appspot.deedleit.server.TimelineServiceEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface TimelineService extends RemoteService {
	ArrayList<TimelineServiceEntity> getTimeline(String email, Integer count,
			Integer skip, String rating, String type, String city,
			String country) throws IllegalArgumentException;
}
