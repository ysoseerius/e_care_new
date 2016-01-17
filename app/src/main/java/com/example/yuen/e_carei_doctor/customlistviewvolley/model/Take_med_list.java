package com.example.yuen.e_carei_doctor.customlistviewvolley.model;

public class Take_med_list {
	private String name, thumbnailUrl;
	private String uid;
	private int aid;
	//private double rating;
	//private ArrayList<String> genre;

	public Take_med_list() {
	}

	public Take_med_list(String name, String thumbnailUrl, String uid,int aid
						 ) {
		this.name = name;
		this.thumbnailUrl = thumbnailUrl;
		this.uid = uid;
		this.aid = aid;
		//this.rating = rating;
		//this.genre = genre;
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

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}
/*
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public ArrayList<String> getGenre() {
		return genre;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}
*/
}
