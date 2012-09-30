package com.appspot.deedleit.server.entity;


public class LoginEntity {
	private String email;
	private String name;
	private String photoId;
	private String folderId;
//	private	Integer like;
//	private Integer unlike;
//	private List<String> likedItems;
//	private List<String> following
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
//	@Override
//	public String toString() {
//		return "Success";
//	}
	
}
