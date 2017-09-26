package kr.bottomtab.pettner;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-13.
 */

public class ReservationObject {
    String id;
    String resDatetime;
    String subject;
    String memo;
    int dogSelect;
    String photo;

    int status;
    String name;

    ArrayList<Integer> idlist=new ArrayList<>();
    void setIdList(Integer id){
        idlist.add(id);
    }
}
