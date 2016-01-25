package com.example.yuen.e_carei_doctor.customlistviewvolley.model;

public class Appointment {
	private String name, thumbnailUrl;
	private int aid;
	//private int year;
	private String uid;
	private int queue;
	private String consultation_type ;
	private String appointment_type;
	private String time;
	private String date;
	//private double rating;
	//private ArrayList<String> genre;

	public Appointment() {
	}

	public Appointment(String name, int aid,String thumbnailUrl, String uid,int queue,String consultation_type,String appointment_type,String time,String date) {
		this.name = name;
		this.thumbnailUrl = thumbnailUrl;
		this.aid=aid;
		this.uid=uid;
		this.queue=queue;
		this.consultation_type =consultation_type;
		this.appointment_type=appointment_type;
		this.time=time;
		this.date=date;
		//this.year = year;
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
		return queue;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}

	public String getCtype() {
		return consultation_type;
	}

	public void setCtype(String consultation_type) {
		this.consultation_type = consultation_type;
	}

	public String getAtype() {
		return appointment_type;
	}

	public void setAtype(String appointment_type) {
		this.appointment_type = appointment_type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
