package com.example.yuen.e_carei;

public class AppointmentList {
	private int queue;
	private String appointment_type;
	private String time;
	private String date;
	private String doctor;

	public AppointmentList() {
	}

	public AppointmentList(int queue,String appointment_type,String time,String date,String doctor) {
		this.appointment_type = appointment_type;
		this.queue=queue;
		this.time=time;
		this.date=date;
		this.doctor=doctor;
	}



	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
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

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

}
