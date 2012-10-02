package com.appspot.deedleit.server.entity;


public class LoginEntity {
	private String email;
	private String name;
	private String photoId;
	private String folderId;
	private String city;
	private String country;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
