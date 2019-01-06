package com.example.administrator.webpasswordprogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class SelectMenuActivity extends AppCompatActivity {
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_ERROR = 400;

    User curUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectmenu);

        ////////////////////////////////activity 전환
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        curUser.setid(id);//현재 로그인 되어있는 유저

        ImageButton commonBtn = (ImageButton) findViewById(R.id.registerBtn);//사이트 등록
        commonBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("id",curUser.getid());
                  startActivity(intent);
            }
        });

        ImageButton QABtn = (ImageButton) findViewById(R.id.checkBtn);//비밀번호 확인
        QABtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                intent.putExtra("id", curUser.getid());
                startActivity(intent);
            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
