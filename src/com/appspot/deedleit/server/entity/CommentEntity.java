package com.appspot.deedleit.server.entity;

public class CommentEntity {
	private String email;
	private String deedId;
	private String comment;
	private String userPhotoId;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDeedId() {
		return deedId;
	}
	public void setDeedId(String photoId) {
		this.deedId = photoId;
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

}
