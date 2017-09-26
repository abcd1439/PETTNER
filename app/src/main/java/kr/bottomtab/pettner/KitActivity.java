package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by user on 2017-06-05.
 */

public class KitActivity extends AppCompatActivity{
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("키트검사");
        toolbar.setTitleTextColor(Color.WHITE);


        final Button firstbtn,secondbtn,thirdbtn;
        Button onebtn,twobtn,threebtn,fourbtn,fivebtn,
                sixbtn,sevenbtn,eightbtn,ninebtn,tenbtn,elevenbtn,
                twelvebtn,thirteenbtn,fourteenbtn,fifteenbtn,sixteenbtn,seventeenbtn;

        ImageView kitbtn;
        kitbtn=(ImageView) findViewById(R.id.kitbtn);

        firstbtn=(Button) findViewById(R.id.firstbtn);
        secondbtn=(Button) findViewById(R.id.secondbtn);
        thirdbtn=(Button) findViewById(R.id.thirdbtn);

        onebtn=(Button) findViewById(R.id.onebtn);
        twobtn=(Button) findViewById(R.id.twobtn);
        threebtn=(Button) findViewById(R.id.threebtn);
        fourbtn=(Button) findViewById(R.id.fourbtn);
        fivebtn=(Button) findViewById(R.id.fivebtn);

        sixbtn=(Button) findViewById(R.id.sixbtn);
        sevenbtn=(Button) findViewById(R.id.sevenbtn);
        eightbtn=(Button) findViewById(R.id.eightbtn);
        ninebtn=(Button) findViewById(R.id.ninebtn);
        tenbtn=(Button) findViewById(R.id.tenbtn);
        elevenbtn=(Button) findViewById(R.id.elevenbtn);

        twelvebtn=(Button) findViewById(R.id.twelvebtn);
        thirteenbtn=(Button) findViewById(R.id.thirteenbtn);
        fourteenbtn=(Button) findViewById(R.id.fourteenbtn);
        fifteenbtn=(Button) findViewById(R.id.fifteenbtn);
        sixteenbtn=(Button) findViewById(R.id.sixteenbtn);
        seventeenbtn=(Button) findViewById(R.id.seventeenbtn);



        onebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstbtn.setBackgroundColor(Color.rgb(254,202,152));
                firstbtn.setTag(0.1);
            }
        });twobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstbtn.setBackgroundColor(Color.rgb(250,168,146));
                firstbtn.setTag(1);
            }
        });threebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstbtn.setBackgroundColor(Color.rgb(235,146,142));
                firstbtn.setTag(2);
            }
        });fourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstbtn.setBackgroundColor(Color.rgb(240,138,134));
                firstbtn.setTag(4);
            }
        });fivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstbtn.setBackgroundColor(Color.rgb(232,107,137));
                firstbtn.setTag(8);
            }
        });


        sixbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(144,208,182));
                secondbtn.setTag(0);
            }
        });sevenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(140,196,135));
                secondbtn.setTag(100);
            }
        });eightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(123,176,86));
                secondbtn.setTag(250);
            }
        });ninebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(140,144,67));
                secondbtn.setTag(500);
            }
        });tenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(129,118,54));
                secondbtn.setTag(1000);
            }
        });elevenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondbtn.setBackgroundColor(Color.rgb(119,85,58));
                secondbtn.setTag(2000);
            }
        });



        twelvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(220,233,119));
                thirdbtn.setTag(0);
            }
        });thirteenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(186,211,109));
                thirdbtn.setTag(1);
            }
        });fourteenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(165,193,119));
                thirdbtn.setTag(30);
            }
        });fifteenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(144,185,17));
                thirdbtn.setTag(100);
            }
        });sixteenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(112,175,154));
                thirdbtn.setTag(300);
            }
        });seventeenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdbtn.setBackgroundColor(Color.rgb(89,156,138));
                thirdbtn.setTag(2000);
            }
        });


        kitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(KitActivity.this,KitSaveActivity.class);

                i.putExtra("value1",(Integer)firstbtn.getTag());
                i.putExtra("value2",(Integer)secondbtn.getTag());
                i.putExtra("value3",(Integer)thirdbtn.getTag());
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(this, SecondBottomTabActivity.class);
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
        Intent i = new Intent(this, SecondBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
