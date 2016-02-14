package com.example.yuen.e_carei;

/**
 * Created by Yuen on 13/2/2016.
 */
public class Patientlist {
    private String no_of_case_history, date_of_case_history;
    //private ArrayList<String> genre;

    public Patientlist() {
    }

    public Patientlist(String noch, String doch) {
        this.no_of_case_history = noch;
        this.date_of_case_history = doch;
    }

    public String getNo_of_case_history() {
        return no_of_case_history;
    }

    public void setNo_of_case_history(String no) {
        this.no_of_case_history = no;
    }

    public String getDate_of_case_history() {
        return date_of_case_history;
    }

    public void setdate_of_case_history(String date) {
        this.date_of_case_history = date;
    }



}
