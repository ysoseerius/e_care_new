package com.example.yuen.e_carei;

public class Time {
	private String time;
	private String date;
	//private ArrayList<String> genre;

	public Time() {
	}

	public Time(String time,String date) {
		this.time = time;
		this.date = date;
		//this.genre = genre;
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
