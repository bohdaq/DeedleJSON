package com.appspot.deedleit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TimelineServiceEntity implements IsSerializable {

	public TimelineServiceEntity(){
		
	}
	
	public TimelineServiceEntity(String title, String email,
			String description, String photoUrl, String city, Long date,
			Double latitude, Double longtitude, Integer like, Integer unlike) {

		this.title = title;
		this.email = email;
		this.description = description;
		this.photoUrl = photoUrl;
		this.city = city;
		this.date = date;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.like = like;
		this.unlike = unlike;
		
	}
	
	public String title;
	public String email;
	public String description;
	public String photoUrl;
	public String city;
	public Long date;
	public Double latitude;
	public Double longtitude;
	public Integer like;
	public Integer unlike;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	public Integer getLike() {
		return like;
	}
	public void setLike(Integer like) {
		this.like = like;
	}
	public Integer getUnlike() {
		return unlike;
	}
	public void setUnlike(Integer unlike) {
		this.unlike = unlike;
	}
}
