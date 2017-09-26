/*
 *   Http 요청 상수값
 */
package kr.bottomtab.pettner;

public class NetworkDefineConstant {
    public static final String HOST_URL = "" +
            "http://";
    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_PUT = "PUT";
    public static final String REQUEST_DELETE = "DELETE";




    public static String SERVER_URL_USER;

    //요청 URL path
    public static String SERVER_URL_PET_HEALTH_ADDSELECT;
    public static String SERVER_URL_PET_SELECT;
    public static String SERVER_URL_PET_ADD;

    public static String SERVER_URL_HOSPITAL;
    public static String SERVER_URL_HOSPITAL_SELECT;

    public static String SERVER_URL_HOSPITAL_RESERVATION_ADDSELECT;
    public static String SERVER_URL_HOSPITAL_RESERVATION_PUT;

    public static String SERVER_URL_QUESTION_ADDSELECT;

    public static String SERVER_URL_QUESTION_SUBSELECT;

    public static String SERVER_URL_PET_EXAMINE_SELECT;

    public static String SERVER_URL_USER_NOTSELECT;

    static{
        SERVER_URL_USER=HOST_URL+"/users/1";
        SERVER_URL_PET_ADD=HOST_URL+"/users/1/animals";
        SERVER_URL_HOSPITAL_SELECT=HOST_URL+"/users/1/myHospital/"; //+hospitalselect
        SERVER_URL_HOSPITAL_RESERVATION_ADDSELECT=HOST_URL+"/users/1/reservations";
        SERVER_URL_QUESTION_ADDSELECT=HOST_URL+"/users/1/qnas";
        SERVER_URL_USER_NOTSELECT=HOST_URL+"/users/1/notis";




        SERVER_URL_PET_SELECT=HOST_URL+"/animals/";

        SERVER_URL_PET_HEALTH_ADDSELECT =
                "/healthItems";
        //server_url_pet_select+dogselect+server_url+pet_health_addselect



        SERVER_URL_HOSPITAL=HOST_URL+"/hospitals";



        SERVER_URL_HOSPITAL_RESERVATION_PUT=HOST_URL+"/reservations/";



        SERVER_URL_QUESTION_SUBSELECT=HOST_URL+"/qnas/";

        SERVER_URL_PET_EXAMINE_SELECT="/prescripts";
        //server_url_pet_select+dogselect+server_url+pet_examine_select



    }

    //다이얼로그 요청값
    public static final int BLOOD_INSERT_DIALOG_OK=1;
    public static final int BLOOD_INSERT_DIALOG_FAIL=2;
}