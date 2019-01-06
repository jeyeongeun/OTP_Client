package com.example.administrator.webpasswordprogram;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.OutputStream;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_LOGIN = 1001;
    public static final int REQUEST_CODE_USER = 1002;

    EditText usernameInput;
    EditText passwordInput;


    JSONObject obj;
    JSONObject jsonObjectIn;//서버로부터 받은 data json으로
    String fromServer;//서버로부터 받은 data


    User joinUser = new User();//회원가입할 유저

    //로그인
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.usernameEntry);
        passwordInput = (EditText) findViewById(R.id.passwordEntry);


        Button loginButton = (Button) findViewById(R.id.loginBtn);//로그인
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                fromServer = "#123";
                ConnectThread thread = new ConnectThread(1);
                thread.start();//서버와 통신
                while(fromServer == "#123");//sleep

                try {//서버로부터 받은 값 자르기
                    jsonObjectIn = (JSONObject) new JSONTokener(fromServer).nextValue();
                    String type = jsonObjectIn.getString("type");
                    String result  = jsonObjectIn.getString("result");

                    if(type.equals("login_RE") && result.equals("fail") ){
                        Toast toast = Toast.makeText(getBaseContext(), "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else{//로그인 가능
                        Intent intent = new Intent(getApplicationContext(), SelectMenuActivity.class);
                        intent.putExtra("id",username);
                        startActivity(intent);//강의검색창으로
                    }
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(getBaseContext(), "예기치 못한 에러가 발생했습니다.", Toast.LENGTH_SHORT);
                    toast.show();

                    e.printStackTrace();
                }





            }
        });


        Button joinButton = (Button) findViewById(R.id.joinBtn);//회원가입
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivityForResult(intent, REQUEST_CODE_USER);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_USER) {//유저로부터 회원가입 정보를 입력받은 후
            if (intent != null) {

                String id =intent.getStringExtra("id");
                String pw =intent.getStringExtra("pw");

                Toast toast = Toast.makeText(getBaseContext(), "id : " + id + ", pw : " + pw, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    class ConnectThread extends Thread {
        int type;

        public ConnectThread(int _type) {
            type = _type;
        }

        public void run() {
            try {
                int port = 5555;
                obj = new JSONObject();

                if (type == 1) {//로그인
                    username = usernameInput.getText().toString();
                    password = passwordInput.getText().toString();

                    System.out.println("sent_login");

                    obj.put("type", "login_RQ");
                    obj.put("id", username);
                    obj.put("pw", password);
                }

              /*  else if (type == 2) {//회원가입
                    obj.put("type", "join_RQ");
                    obj.put("session_key", "no");
                    obj.put("content", joinUser.getid() +"$$" + joinUser.getpw() +"$$" + joinUser.getname() +"$$" + "1" +"$$" +
                            joinUser.getemail());
                }*/

                Socket sock = new Socket("172.16.206.74", port);
                OutputStream os = sock.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(obj.toString());
                System.out.println("sent");

                InputStream in = sock.getInputStream();
                DataInputStream dis = new DataInputStream(in);

                fromServer = dis.readUTF();
                System.out.println(fromServer);
                dos.close();
                os.close();
                sock.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



}

