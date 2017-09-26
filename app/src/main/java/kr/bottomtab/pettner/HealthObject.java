package kr.bottomtab.pettner;


public class HealthObject {
    public int id;
    public int type;
    public int recordFloat;
    public String date;
    public String time;
    public String doctor;

    public String prescriptIcon;
    public String prescriptText;
    HealthObject(){}
    void getDT(String createdAt){
        this.date =createdAt.substring(0,10);
        this.time=createdAt.substring(11,16);
    }

}
