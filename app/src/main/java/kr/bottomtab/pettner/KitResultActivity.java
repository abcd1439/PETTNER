package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017-06-08.
 */

public class KitResultActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView date,kor,eng,status,number,pretext,doctorname;
    ImageView color,preicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kitresult);


        Intent i=getIntent();
        String data=i.getStringExtra("value");
        int data1=i.getIntExtra("value1",-1);
        String data2=i.getStringExtra("value2");
        String data3=i.getStringExtra("value3");
        String doctor=i.getStringExtra("doctor");
        Log.e("tt",data2);
        int type=i.getIntExtra("type",-1);
        if(doctor==null){
            doctor="";
        }
        if(data3.equals("null")){
            data3="종합의견이 아직 입력되지 않았습니다.";
        }
        doctor+=" 수의사님의 코치";

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("키트 결과");
        toolbar.setTitleTextColor(Color.WHITE);

        date=(TextView) findViewById(R.id.date);
        kor=(TextView) findViewById(R.id.kor);
        eng=(TextView) findViewById(R.id.eng);
        status=(TextView) findViewById(R.id.status);
        number=(TextView) findViewById(R.id.number);
        color=(ImageView) findViewById(R.id.color);
        pretext=(TextView) findViewById(R.id.pretext);
        preicon=(ImageView) findViewById(R.id.preicon);
        doctorname=(TextView) findViewById(R.id.doctorname);

        date.setText(data+" 키트 종합 결과");

        if(type==1) {
            kor.setText("간장");
            eng.setText("(Urobilinogen)");
            if (data1 == 0.1 || data1 == 0) {
                color.setBackgroundColor(Color.rgb(254, 202, 152));
                status.setText("정상");
                status.setTextColor(Color.rgb(33, 125, 251));
                data1 = 0;
            } else if (data1 == 1) {
                color.setBackgroundColor(Color.rgb(250, 168, 146));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255, 64, 129));
            } else if (data1 == 2) {
                color.setBackgroundColor(Color.rgb(235, 146, 142));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255, 64, 129));
            } else if (data1 == 4) {
                color.setBackgroundColor(Color.rgb(240, 138, 134));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255, 64, 129));
            } else if (data1 == 8) {
                color.setBackgroundColor(Color.rgb(232, 107, 137));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255, 64, 129));
            }
            number.setText(Integer.toString(data1));
        }else if(type==2){
            kor.setText("당뇨");
            eng.setText("(Glucose)");
            if(data1==0){
                color.setBackgroundColor(Color.rgb(144,208,182));
                status.setText("정상");
                status.setTextColor(Color.rgb(33,125,251));
            }else if(data1==100){
                color.setBackgroundColor(Color.rgb(140,196,135));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==250){
                color.setBackgroundColor(Color.rgb(123,176,86));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==500){
                color.setBackgroundColor(Color.rgb(140,144,67));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==1000){
                color.setBackgroundColor(Color.rgb(129,118,54));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==2000){
                color.setBackgroundColor(Color.rgb(119,85,58));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }
            number.setText(Integer.toString(data1));
        }else{
            kor.setText("신장");
            eng.setText("(Protein)");
            if(data1==0){
                color.setBackgroundColor(Color.rgb(220,233,119));
                status.setText("정상");
                status.setTextColor(Color.rgb(33,125,251));
            }else if(data1==1){
                color.setBackgroundColor(Color.rgb(186,211,109));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==30){
                color.setBackgroundColor(Color.rgb(165,193,119));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==100){
                color.setBackgroundColor(Color.rgb(144,185,17));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==300){
                color.setBackgroundColor(Color.rgb(112,175,154));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }else if(data1==2000){
                color.setBackgroundColor(Color.rgb(89,156,138));
                status.setText("비정상");
                status.setTextColor(Color.rgb(255,64,129));
            }
            number.setText(Integer.toString(data1));

        }
        doctorname.setText(doctor);

        pretext.setText(data3);
        if(data2.equals("1")){
            preicon.setImageResource(R.drawable.face1);
        }else if(data2.equals("2")){
            preicon.setImageResource(R.drawable.face2);
        }else if(data2.equals("3")){
            preicon.setImageResource(R.drawable.face3);
        }else if(data2.equals("4")){
            preicon.setImageResource(R.drawable.face4);
        }else if(data2.equals("5")){
            preicon.setImageResource(R.drawable.face5);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(this, PersonalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PersonalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
