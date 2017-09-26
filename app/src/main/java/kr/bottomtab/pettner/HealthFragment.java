package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HealthFragment extends Fragment {
    public static final String LIVER_KEY="liverKey";
    public static final String DIABETES_KEY="diabetesKey";
    public static final String KIDNEY_KEY="kidneyKey";
    public static final String DATE_KEY="dateKey";

    public HealthFragment() {
    }
    public static HealthFragment newInstance(int data1, int data2, int data3, String data4){
        HealthFragment f = new HealthFragment();
        Bundle b = new Bundle();
        b.putInt(LIVER_KEY, data1);
        b.putInt(DIABETES_KEY, data2);
        b.putInt(KIDNEY_KEY, data3);
        b.putString(DATE_KEY, data4);

        f.setArguments(b);

        return f;
    }
    int data1;
    int data2;
    int data3;
    String data4;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if( b != null){
            data1 = b.getInt(LIVER_KEY);
            data2 = b.getInt(DIABETES_KEY);
            data3 = b.getInt(KIDNEY_KEY);
            data4 = b.getString(DATE_KEY);
        }

    }

    TextView text1,text2,text3,text4,status1,status2,status3;
    ImageView color1,color2,color3;


    FirstBottomTabActivity owner;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_health,container, false);

        owner = (FirstBottomTabActivity)getActivity();
        text1=(TextView) v.findViewById(R.id.data1);
        text2=(TextView) v.findViewById(R.id.data2);
        text3=(TextView) v.findViewById(R.id.data3);
        text4=(TextView) v.findViewById(R.id.text_message);

        text1.setText(Integer.toString(data1));
        text2.setText(Integer.toString(data2));
        text3.setText(Integer.toString(data3));
        text4.setText(data4);

        status1=(TextView) v.findViewById(R.id.status1);
        status2=(TextView) v.findViewById(R.id.status2);
        status3=(TextView) v.findViewById(R.id.status3);

        color1=(ImageView) v.findViewById(R.id.color1);
        color2=(ImageView) v.findViewById(R.id.color2);
        color3=(ImageView) v.findViewById(R.id.color3);



        if(data1==0.1||data1==-1){
            color1.setBackgroundColor(Color.rgb(254,202,152));
            status1.setText("정상");
            status1.setTextColor(Color.rgb(33,125,251));
            data1=0;
        }else if(data1==1){
            color1.setBackgroundColor(Color.rgb(250,168,146));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==2){
            color1.setBackgroundColor(Color.rgb(235,146,142));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==4){
            color1.setBackgroundColor(Color.rgb(240,138,134));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==8){
            color1.setBackgroundColor(Color.rgb(232,107,137));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }

        if(data2==0||data2==-1){
            color2.setBackgroundColor(Color.rgb(144,208,182));
            status2.setText("정상");
            status2.setTextColor(Color.rgb(33,125,251));
            data2=0;
        }else if(data2==100){
            color2.setBackgroundColor(Color.rgb(140,196,135));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==250){
            color2.setBackgroundColor(Color.rgb(123,176,86));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==500){
            color2.setBackgroundColor(Color.rgb(140,144,67));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==1000){
            color2.setBackgroundColor(Color.rgb(129,118,54));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==2000){
            color2.setBackgroundColor(Color.rgb(119,85,58));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }


        if(data3==0||data3==-1){
            color3.setBackgroundColor(Color.rgb(220,233,119));
            status3.setText("정상");
            status3.setTextColor(Color.rgb(33,125,251));
            data3=0;
        }else if(data3==1){
            color3.setBackgroundColor(Color.rgb(186,211,109));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==30){
            color3.setBackgroundColor(Color.rgb(165,193,119));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==100){
            color3.setBackgroundColor(Color.rgb(144,185,17));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==300){
            color3.setBackgroundColor(Color.rgb(112,175,154));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==2000){
            color3.setBackgroundColor(Color.rgb(89,156,138));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }


        Button personalbtn=(Button)v.findViewById(R.id.personalbtn);
        personalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(HealthFragment.super.getActivity(), PersonalActivity.class);
                startActivity(i);
            }
        });


        return v;
    }
}