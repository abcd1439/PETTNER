package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-05-31.
 */

public class QuestionActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView date;
    EditText questiontext;
    QuestionObject entityObject=new QuestionObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);

        toolbar = (Toolbar) findViewById(R.id.qtoolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("Q\u0026A");
        toolbar.setTitleTextColor(Color.WHITE);

        date=(TextView) findViewById(R.id.rdate);
        Calendar c=Calendar.getInstance();
        date.setText(c.get(c.YEAR)+"/"+(c.get(c.MONTH)+1)+"/"+c.get(c.DATE));

        questiontext=(EditText) findViewById(R.id.questiontext);


        ImageView questionbtn=(ImageView) findViewById(R.id.qquestionbtn);
        questionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questiontext.getText().length()==0){
                    Toast.makeText(QuestionActivity.this,"질문을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    entityObject.question=questiontext.getText().toString();

                    new QuestionAdd().execute(entityObject);


                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(QuestionActivity.this, ThirdBottomTabActivity.class);
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
        Intent i= new Intent(QuestionActivity.this, ThirdBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    public class QuestionAdd extends AsyncTask<QuestionObject,Integer,String>{

        @Override
        protected String doInBackground(QuestionObject... params) {
            boolean flag;

            QuestionObject reqParams=params[0];
            Response response = null;
            OkHttpClient toServer;
            Log.e("tt",reqParams.question);
            Log.e("tt",MySharedPreferencesManager.getInstance().getHospitalselect());
            try {
                toServer = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅
                RequestBody requestBody = new FormBody.Builder()
                        .add("question", reqParams.question)
                        .add("hospitalId",MySharedPreferencesManager.getInstance().getHospitalselect())
                        .build();


                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_QUESTION_ADDSELECT)
                        .post(requestBody)
                        .build();
                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();
                String returedJSON;
                if( flag ){ //성공했다면
                    returedJSON = response.body().string();
                    Log.e("tt","------------------------------------");
                    Log.e("resultJSON", returedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returedJSON);

                    }catch(JSONException jsone){
                        Log.e("json에러", jsone.toString());
                    }
                }else{
                    //요청에러 발생시(http 에러)
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
            /*Intent i= new Intent(QuestionActivity.this, ThirdBottomTabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();*/
        }
    }


}
