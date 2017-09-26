package kr.bottomtab.pettner;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-06-08.
 */

public class QuestionAnswerActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    QanswerAdapter adapter;

    EditText content;
    Calendar c = Calendar.getInstance();
    int cyear, cmonth, cdate;
    String m;

    TextView user,date,quest,total;
    InputMethodManager imm;


    ArrayList<QuestionObject> subquestion=new ArrayList<>();
    ArrayList<String> question=new ArrayList<>();
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionanswer);

        Intent i = getIntent();
        final int data = i.getIntExtra("value", -1);
        new QuestionAnswerAsync().execute(data);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요



        toolbar.setTitle("Q\u0026A 답변");
        toolbar.setTitleTextColor(Color.WHITE);


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        user=(TextView) findViewById(R.id.user);
        date=(TextView) findViewById(R.id.date);
        quest=(TextView) findViewById(R.id.question);
        total=(TextView) findViewById(R.id.total);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);

        content=(EditText) findViewById(R.id.content);
        Button send=(Button) findViewById(R.id.send);


        cyear = c.get(c.YEAR);
        cmonth = c.get(c.MONTH) + 1;
        m = Integer.toString(cmonth);
        if (m.length() == 1) {
            m = "0" + m;
        }

        cdate = c.get(c.DATE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionObject q=new QuestionObject();
                q.content= content.getText().toString();
                q.id= Integer.parseInt(MySharedPreferencesManager.getInstance().getDogselect());
                q.subdate=cyear+"-"+m+"-"+cdate+"T"+"00:00:00.000Z";
                q.doctorname="사용자";
                q.qnaId=data;
                subquestion.add(q);
                recyclerView.setLayoutManager(layoutManager);
                adapter=new QanswerAdapter(subquestion);
                recyclerView.setAdapter(adapter);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                content.setText("");
                new AsyncRepeat().execute(q);
            }
        });

    }

    public static class QanswerAdapter extends RecyclerView.Adapter<QanswerAdapter.ViewHolder> {
        ArrayList<String> content=new ArrayList<>();
        ArrayList<String> subdate=new ArrayList<>();
        ArrayList<String> doctorname=new ArrayList<>();
        View view;

        public QanswerAdapter(ArrayList<QuestionObject> question) {
            for(int i=0;i<question.size();i++) {
                this.content.add(question.get(i).content);
                this.subdate.add(question.get(i).subdate.substring(0,10));
                this.doctorname.add(question.get(i).doctorname);//question.get(i).doctorname
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subdate,content,doctorname;
            CircleImageView profile;
            public ViewHolder(View v) {
                super(v);
                subdate=(TextView) v.findViewById(R.id.subdate);
                content=(TextView) v.findViewById(R.id.content);
                doctorname=(TextView) v.findViewById(R.id.doctorname);
                profile=(CircleImageView) v.findViewById(R.id.profile);
            }
        }

        @Override
        public QanswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_questionanswer, parent, false);

            return new QanswerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(QanswerAdapter.ViewHolder holder, final int position) {
            holder.content.setText(content.get(position));
            holder.subdate.setText(subdate.get(position));
            holder.doctorname.setText(doctorname.get(position));
            if(!doctorname.get(position).equals("사용자")){
                holder.profile.setImageResource(R.drawable.icon_hospital);
            }
        }

        @Override
        public int getItemCount() {
            return content.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent i = new Intent(this, ThirdBottomTabActivity.class);
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
        Intent i = new Intent(this, ThirdBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private class QuestionAnswerAsync extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Response response = null;
            OkHttpClient toClient;

            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_QUESTION_SUBSELECT+params[0])
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt", "Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject jsonData=jsonObject.getJSONObject("result");
                    question.add(jsonData.getString("question"));
                    question.add(jsonData.getString("createdAt").substring(0,10));

                    /*JSONObject jo=jsonData.getJSONObject("user");
                    question.add(jo.getString("name"));*/

                    JSONArray json=jsonData.getJSONArray("qnaSubs");
                    int jsize=json.length();
                    for(int i=0;i<jsize;i++){
                        QuestionObject q=new QuestionObject();
                        JSONObject j=json.getJSONObject(i);
                        q.content=j.getString("content");
                        q.subdate=j.getString("createdAt");

                        if(j.isNull("vetId")){
                            q.doctorname="사용자";
                        }else{
                            q.doctorname="의사";
                        }

                        /*if(j.isNull("vet")){
                            JSONObject jj=j.getJSONObject("user");
                            q.doctorname=jj.getString("name");
                        }else{
                            JSONObject jso=j.getJSONObject("vet");
                            q.doctorname=jso.getString("name");
                        }*/
                        subquestion.add(q);

                    }


                } catch (JSONException jsone) {
                    Log.e("json에러", jsone.toString());
                }


            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close(); //3.* 이상에서는 반드시 닫아 준다.
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            quest.setText(question.get(0));
            date.setText(question.get(1));
            user.setText("사용자");

            total.setText("답변 "+Integer.toString(subquestion.size())+"개");
            recyclerView.setLayoutManager(layoutManager);
            adapter=new QanswerAdapter(subquestion);
            recyclerView.setAdapter(adapter);


        }

    }


    private class AsyncRepeat extends AsyncTask<QuestionObject,Integer,String>{

        @Override
        protected String doInBackground(QuestionObject... params) {
            boolean flag;
            Response response = null;
            OkHttpClient toServer;
            try{

                //요청 Form세팅
                toServer = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅
                RequestBody postBody = new FormBody.Builder()
                        .add("userId",NetworkDefineConstant.SERVER_URL_USER.substring(NetworkDefineConstant.SERVER_URL_USER.length()-1) )
                        .add("content",params[0].content)
                        .build();

                Log.e("tt","aaaaaaaaaaaa");
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_QUESTION_SUBSELECT+params[0].qnaId)
                        .post(postBody)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                flag = response.isSuccessful();
                String returedJSON;
                if( flag ){ //성공했다면
                    returedJSON = response.body().string();
                    Log.e("tt", returedJSON);

                }else{
                    //요청에러 발생시(http 에러)
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
    }
}