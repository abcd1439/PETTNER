package kr.bottomtab.pettner;


public class CoachObject {

    public static String hospital="병원";

    public static String phone="031";

    public static String reservation="예약";

    public static String title="질문 제목";







    static String getHospital() {
        return hospital;
    }

    static String getPhone(){
        return phone;
    }


    static String getReservation(){
        return reservation;
    }

    static String getTitle(){
        return title;
    }



    /*    private static ArrayList<Integer> girlImageResources;
    private static ArrayList<Integer> boyImageResources;

    static {
        girlImageResources = new ArrayList<Integer>();
        boyImageResources = new ArrayList<>();

        girlImageResources.add(R.drawable.girls_generation_yuna);
        girlImageResources.add(R.drawable.girls_generation_tifany);
        girlImageResources.add(R.drawable.girls_generation_jesica);
        girlImageResources.add(R.drawable.girls_generation_seohyun);
        girlImageResources.add(R.drawable.girls_generation_sunny);
        girlImageResources.add(R.drawable.girls_generation_suyoung);
        girlImageResources.add(R.drawable.girls_generation_taeyeon);
        girlImageResources.add(R.drawable.girls_generation_hyoyeon);
        girlImageResources.add(R.drawable.girls_generation_yuri);


        boyImageResources.add(R.drawable.mini1);
        boyImageResources.add(R.drawable.mini2);
        boyImageResources.add(R.drawable.mini3);
        boyImageResources.add(R.drawable.mini4);
        boyImageResources.add(R.drawable.mini5);
        boyImageResources.add(R.drawable.mini6);
        boyImageResources.add(R.drawable.mini7);
        boyImageResources.add(R.drawable.mini8);
    }
    private static HashMap<Integer, String> nameMaps;

    static {
        nameMaps = new HashMap<>();

        nameMaps.put( R.drawable.girls_generation_seohyun, "서현");
        nameMaps.put(R.drawable.girls_generation_sunny, "써니");
        nameMaps.put(R.drawable.girls_generation_jesica, "제시카");
        nameMaps.put( R.drawable.girls_generation_hyoyeon, "효연");
        nameMaps.put( R.drawable.girls_generation_seohyun, "서현");
        nameMaps.put( R.drawable.girls_generation_suyoung, "수영");
        nameMaps.put(R.drawable.girls_generation_taeyeon, "태연");
        nameMaps.put( R.drawable.girls_generation_tifany, "티파니");
        nameMaps.put(R.drawable.girls_generation_yuri, "유리");
        nameMaps.put(R.drawable.girls_generation_yuna, "윤아");


        nameMaps.put(R.drawable.mini1, "미니1");
        nameMaps.put(R.drawable.mini2, "미니2");
        nameMaps.put(R.drawable.mini3, "미니3");
        nameMaps.put(R.drawable.mini4, "미니4");
        nameMaps.put(R.drawable.mini5, "미니5");
        nameMaps.put(R.drawable.mini6, "미니6");
        nameMaps.put(R.drawable.mini7, "미니7");
        nameMaps.put(R.drawable.mini8, "미니8");
    }



    public static ArrayList<Integer> getGirlsImage() {
        return girlImageResources;
    }
    public static String getGirlGroupName(Integer key) {
        return nameMaps.get(key);
    }

    private static Random random = new Random(System.currentTimeMillis());

    public static ArrayList<Integer> getBoysImage() {
        Collections.shuffle(boyImageResources, random);
        return boyImageResources;
    }
    public static String getExoGroupName(Integer key) {
        return nameMaps.get(key);
    }*/
}
