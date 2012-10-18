package com.appspot.deedleit.client;

import com.google.gwt.core.client.JavaScriptObject;

public class TimelineData extends JavaScriptObject {
	
	protected TimelineData() {
	}

	public final native String getEmail() /*-{
		return this.email;
	}-*/; 

	public final native String getPhotoId() /*-{
		return this.photoId;
	}-*/;
	
	public final native String getName() /*-{
		return this.name;
	}-*/;
	
	public final native String getUserPhotoId() /*-{
		return this.userPhotoId;
	}-*/;

	public final native String getTitle() /*-{
		return this.title;
	}-*/;

	public final native Long getDate() /*-{
		return this.date;
	}-*/;
	
	public final native int getLike() /*-{
		return this.like;
	}-*/;
	
	public final native int getUnlike() /*-{
		return this.unlike;
	}-*/;
	
	public final native double getLatitude() /*-{
		return this.latitude;
	}-*/;

	public final native double getLongtitude() /*-{
		return this.longtitude;
	}-*/;

}
