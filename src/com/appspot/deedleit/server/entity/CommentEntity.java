package com.appspot.deedleit.server.entity;

public class CommentEntity {
	private String email;
	private String photoId;
	private String comment;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSingleComment() {
		StringBuilder comment = new StringBuilder();
		comment.append(email);
		comment.append(SEPARATOR);
		comment.append(photoId);
		comment.append(SEPARATOR);
		comment.append(comment);
		comment.append(SEPARATOR);
		return comment.toString();
	}
	private static final String SEPARATOR = "#$*";
}
