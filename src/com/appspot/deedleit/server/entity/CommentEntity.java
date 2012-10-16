package com.appspot.deedleit.server.entity;

public class CommentEntity {
	private String email;
	private String deedId;
	private String comment;
	private String userPhotoId;
	private String name;
	private Long date;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDeedId() {
		return deedId;
	}
	public void setDeedId(String deedId) {
		this.deedId = deedId;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getUserPhotoId() {
		return userPhotoId;
	}
	public void setUserPhotoId(String userPhotoId) {
		this.userPhotoId = userPhotoId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}

}
