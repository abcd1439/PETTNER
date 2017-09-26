package kr.bottomtab.pettner;

/**
 * Created by user on 2017-06-14.
 */

public class PrescriptObject {
    String prescriptDate;
    String prescriptTime;
    String problem;
    String answer;
    void getDT(String date){
        this.prescriptDate=date.substring(0,10);
        this.prescriptTime=date.substring(11,16);
    }
}
