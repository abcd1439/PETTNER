package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-06-05.
 */

public class AlarmActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AlarmAdapter adapter;

    ArrayList<String> alarm=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("알림");
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        Button delete=(Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncDelete().execute();
            }
        });

        new AsyncAlarm().execute();




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(this, FirstBottomTabActivity.class);
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
        Intent i = new Intent(this, FirstBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
        View view;
        ArrayList<String> al=new ArrayList<>();
        public AlarmAdapter(ArrayList<String> a){
            Log.e("tt", a.size()+"");
            al=a;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView content;
            public ViewHolder(View v){
                super(v);
                content=(TextView)v.findViewById(R.id.content);
            }
        }
        @Override
        public AlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_alarm, parent, false);
            return new AlarmAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmAdapter.ViewHolder holder, int position) {
            holder.content.setText(al.get(position));
        }

        @Override
        public int getItemCount() {
            return al.size();
        }
    }

    private class AsyncAlarm extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER_NOTSELECT)
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt","Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONArray jsonData=jsonObject.getJSONArray("data");

                    for(int i=0;i<jsonData.length();i++){

                        JSONObject j=jsonData.getJSONObject(i);
                        alarm.add(j.getString("content"));
                        Log.e("tt",alarm.get(i));

                    }



                }catch(JSONException jsone){
                    Log.e("json에러", jsone.toString());
                }


            }catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally{
                if(response != null) {
                    response.close(); //3.* 이상에서는 반드시 닫아 준다.
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            adapter = new AlarmAdapter(alarm);
            recyclerView.setAdapter(adapter);
        }
    }

    private class AsyncDelete extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;
            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER_NOTSELECT)
                        .delete()
                        .build();

                response = toClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected  void onPostExecute(String result) {
            alarm.clear();
            adapter = new AlarmAdapter(alarm);
            recyclerView.setAdapter(adapter);
        }
    }
}
