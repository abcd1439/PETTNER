package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class HospitalFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String HOSPITAL_NAME = "hospitalName";
    private static final String HOSPITAL_LOCATION = "hospitalLocation";
    private static final String HOSPITAL_ADDRESS = "hospitalAddress";
    private static final String HOSPITAL_PHONE = "hospitalPhone";
    private static final String HOSPITAL_DS = "hospitalDs";
    private static final String HOSPITAL_DF = "hospitalDf";
    private static final String HOSPITAL_ES = "hospitalEs";
    private static final String HOSPITAL_EF = "hospitalEf";
    private static final String HOSPITAL_DLS = "hospitalDls";
    private static final String HOSPITAL_DLF = "hospitalDlf";
    private static final String HOSPITAL_ELS = "hospitalEls";
    private static final String HOSPITAL_ELF = "hospitalElf";
    private static final String HOSPITAL_PHOTO = "hospitalPhoto";
    private static final String HOSPITAL_VACATION = "hospitalVacation";
    private static final String HOSPITAL_SUBJECT = "hospitalSubject";
    private static final String HOSPITAL_INTRO = "hospitalIntro";


    private static final String RECYCLER_DATA = "reData";

    int flag=0;
    private String title;
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
    String photo;
    String rephoto;
    String rename;
    String resubejct;
    String introduce;


    protected RecyclerView recyclerView;
    public MyAdapter adapter;

    public HospitalFragment() {
    }

    public static HospitalFragment newInstance(String title, HospitalObject h, ArrayList<HospitalObject> re) {
        HospitalFragment fragment = new HospitalFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(HOSPITAL_NAME,h.name);
        args.putString(HOSPITAL_LOCATION,h.location);
        args.putString(HOSPITAL_ADDRESS,h.address);
        args.putString(HOSPITAL_PHONE,h.phone);
        args.putString(HOSPITAL_DS,h.daysStart);
        args.putString(HOSPITAL_DF,h.daysFinish);
        args.putString(HOSPITAL_ES,h.endStart);
        args.putString(HOSPITAL_EF,h.endFinish);
        args.putString(HOSPITAL_DLS,h.daysLunchStart);
        args.putString(HOSPITAL_DLF,h.daysLunchFinish);
        args.putString(HOSPITAL_ELS,h.endLunchStart);
        args.putString(HOSPITAL_ELF,h.endLunchFinish);

        args.putString(HOSPITAL_PHOTO,h.photomain);

        args.putString(HOSPITAL_INTRO,h.introduce);
        args.putStringArrayList(HOSPITAL_VACATION,h.vacationDate);
        args.putStringArrayList(HOSPITAL_SUBJECT,h.subject);


        args.putSerializable(RECYCLER_DATA,re);


        fragment.setArguments(args);
        return fragment;
    }



    ArrayList<String> s=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<HospitalObject> re=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name=getArguments().getString(HOSPITAL_NAME);
            title = getArguments().getString(TITLE);
            location= getArguments().getString(HOSPITAL_LOCATION);
            address = getArguments().getString(HOSPITAL_ADDRESS);
            phone= getArguments().getString(HOSPITAL_PHONE);
            daysStart = getArguments().getString(HOSPITAL_DS);
            daysFinish = getArguments().getString(HOSPITAL_DF);
            endStart = getArguments().getString(HOSPITAL_ES);
            endFinish = getArguments().getString(HOSPITAL_EF);
            daysLunchStart = getArguments().getString(HOSPITAL_DLS);
            daysLunchFinish = getArguments().getString(HOSPITAL_DLF);
            endLunchStart = getArguments().getString(HOSPITAL_ELS);
            endLunchFinish = getArguments().getString(HOSPITAL_ELF);
            photo = getArguments().getString(HOSPITAL_PHOTO);
            introduce=getArguments().getString(HOSPITAL_INTRO);
            date=getArguments().getStringArrayList(HOSPITAL_VACATION);
            s= getArguments().getStringArrayList(HOSPITAL_SUBJECT);


            re=(ArrayList<HospitalObject>)getArguments().getSerializable(RECYCLER_DATA);


        }
        getArguments().remove(RECYCLER_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospital, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getContext(),LinearLayoutManager.VERTICAL,false));

        TextView hname,hdshdf,heshef,hdlshdlf,helshelf,hvacation,hphone,haddress,hsubject,reservtext,map,intro;


        hname=(TextView) rootView.findViewById(R.id.hname);
        hdshdf=(TextView) rootView.findViewById(R.id.hdshdf);
        heshef=(TextView) rootView.findViewById(R.id.heshef);
        hdlshdlf=(TextView) rootView.findViewById(R.id.hdlshdlf);
        helshelf=(TextView) rootView.findViewById(R.id.helshelf);
        hvacation=(TextView) rootView.findViewById(R.id.hvacation);
        hphone=(TextView) rootView.findViewById(R.id.hphone);
        haddress=(TextView) rootView.findViewById(R.id.haddress);
        hsubject=(TextView) rootView.findViewById(R.id.hsubject);
        reservtext=(TextView) rootView.findViewById(R.id.reservtext);
        intro=(TextView) rootView.findViewById(R.id.introduce);
        reservtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ReservationActivity.class);
                startActivity(intent);
            }
        });

        map=(TextView) rootView.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),MapActivity.class);
                startActivity(i);
            }
        });

        hname.setText(name);

        hdshdf.setText("평일: "+daysStart.substring(0,5)+" ~ "+daysFinish.substring(0,5));
        heshef.setText("주말: "+endStart.substring(0,5)+" ~ "+endFinish.substring(0,5));
        hdlshdlf.setText("점심: "+daysLunchStart.substring(0,5)+" ~ "+daysLunchFinish.substring(0,5));
        helshelf.setText("점심: "+endLunchStart.substring(0,5)+" ~ "+endLunchFinish.substring(0,5));
        if(introduce.equals("null")){
            introduce="병원소개가 입력되지 않았습니다.";
        }
        intro.setText(introduce);

        hphone.setText(phone);
        haddress.setText(address);
        StringBuilder sb = new StringBuilder("");
        int fsize1=s.size();
        for(int i=0;i<s.size();i++){
            sb.append(s.get(i));
            if(fsize1>1){
                sb.append(", ");
                fsize1-=1;
            }
        }
        hsubject.setText(sb);


        StringBuilder dat = new StringBuilder("");
        int fsize2=date.size();

        for(int i=0;i<date.size();i++){


            dat.append(date.get(i).substring(5,10).replace("-","/"));
            if(fsize2>1){
                dat.append(", ");
                fsize2-=1;
            }
        }
        hvacation.setText("휴무: "+dat);
        Log.e("tt",dat.toString());



        ImageView back=(ImageView) rootView.findViewById(R.id.back);
        Glide.with(BaseApplication.getContext()).load(photo).centerCrop().error(R.drawable.origin_back).into(back);
        back.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        final ImageView heart=(ImageView) rootView.findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),MapActivity.class);
                startActivity(i);
            }
        });

        ImageView questionBtn=(ImageView) rootView.findViewById(R.id.questionbtn);
        ImageView reservationBtn=(ImageView) rootView.findViewById(R.id.reservbtn);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ReservationActivity.class);
                startActivity(intent);
            }
        });
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),QuestionActivity.class);
                startActivity(intent);
            }
        });


        adapter = new MyAdapter(re);
        recyclerView.setAdapter(adapter);

        return rootView;
    }




    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<String> photo=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();
        ArrayList<String> subject=new ArrayList<>();

        View view;

        public MyAdapter(ArrayList<HospitalObject> info) {
            for(int i=0;i<info.size();i++){
                this.name.add(info.get(i).name);
                this.subject.add(info.get(i).hospitalSubjects);
                this.photo.add(info.get(i).photosub);

            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView rename,resubject;
            ImageView photosub;

            public ViewHolder(View v) {
                super(v);
                rename=(TextView) v.findViewById(R.id.rename);
                resubject=(TextView) v.findViewById(R.id.resubject);
                photosub=(ImageView) v.findViewById(R.id.photosub);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_hospital, parent, false);



            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.rename.setText(name.get(position));
            holder.resubject.setText(subject.get(position));
            Glide.with(BaseApplication.getContext()).load(photo.get(position)).centerCrop().override(95,95).error(R.drawable.icon_profile).into(holder.photosub);
        }
        @Override
        public int getItemCount() {
            return name.size();
        }
    }

}
