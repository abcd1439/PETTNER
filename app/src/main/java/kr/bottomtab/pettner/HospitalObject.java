package kr.bottomtab.pettner;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-12.
 */

public class HospitalObject {
    String name;
    String location;
    String address;
    String phone;
    String daysStart;
    String daysFinish;
    String endStart;
    String endFinish;
    String daysLunchStart;
    String daysLunchFinish;
    String endLunchStart;
    String endLunchFinish;
    String photomain;
    String photosub;
    ArrayList<String> vacationDate=null;
    ArrayList<String> subject=null;
    String hospitalSubjects;
    String vets;
    String introduce;

    public void setVacation(ArrayList<String> v){
        vacationDate=v;
    }
    public void setSubject(ArrayList<String> s){
        subject=s;
    }
    public ArrayList<String> getVacation(){
        return vacationDate;
    }
    public ArrayList<String> getSubject(){
        return subject;
    }
}
