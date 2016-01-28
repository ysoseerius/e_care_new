package com.example.yuen.e_carei_search.customsearchlistvolley.model;

public class Search_list {
	private String name, thumbnailUrl;
	private String uid;
	private String hkid;

	public Search_list() {
	}

	public Search_list(String name, String thumbnailUrl, String uid, String hkid) {
		this.name = name;
		this.thumbnailUrl = thumbnailUrl;
		this.uid = uid;
		this.hkid=hkid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String gethkid() {
		return uid;
	}

	public void sethkid(String hkid) {
		this.hkid = hkid;
	}
}
