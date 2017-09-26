package kr.bottomtab.pettner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by user on 2017-06-11.
 */

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;

    ImageView loginbtn;
    EditText idView, passwordView;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public static final String PREF_NAME = "prefs";
    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";

    LoginButton login_button;
    CallbackManager callbackManager;

    CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);





        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.logo); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        toolbar.setTitle("로그인");
        toolbar.setTitleTextColor(Color.WHITE);

        check=(CheckBox) findViewById(R.id.check);

        mPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();


        idView = (EditText)findViewById(R.id.edit_id);
        passwordView = (EditText)findViewById(R.id.edit_password);

        loginbtn=(ImageView) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check.isChecked()){
                    String id = idView.getText().toString();
                    String password = passwordView.getText().toString();
                    MySharedPreferencesManager.getInstance().setId(id);
                    MySharedPreferencesManager.getInstance().setPassword(password);
                }else{
                    MySharedPreferencesManager.getInstance().setId("");
                    MySharedPreferencesManager.getInstance().setPassword("");
                }


                Intent i= new Intent(LoginActivity.this, FirstBottomTabActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        idView.setText(mPrefs.getString(KEY_ID, ""));
        passwordView.setText(mPrefs.getString(KEY_PASSWORD,""));
        idView.setText(MySharedPreferencesManager.getInstance().getId());
        passwordView.setText(MySharedPreferencesManager.getInstance().getPassword());

        initializeControls();
        loginWithFB();
    }

    private void initializeControls(){
        login_button=(LoginButton) findViewById(R.id.login_button);
        callbackManager=CallbackManager.Factory.create();
    }


    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent i=new Intent(LoginActivity.this,FirstBottomTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(),"로그인 실패",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplication(),"로그인 에러",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

}