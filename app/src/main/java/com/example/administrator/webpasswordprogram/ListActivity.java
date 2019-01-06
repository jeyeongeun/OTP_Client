package com.example.administrator.webpasswordprogram;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ListActivity extends AppCompatActivity{
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_ERROR = 400;

    IconTextListAdapter mainadapter;
    ListView listView1;
    User loginUser = new User();
    JSONObject obj;
    JSONObject jsonObjectIn;//서버로부터 받은 data json으로
    String fromServer;//서버로부터 받은 data

    ArrayList<WebSite> mWebSite = new ArrayList<WebSite>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mainadapter = new IconTextListAdapter(this);
        listView1 = (ListView) findViewById(R.id.listView1);


        ////////////////////////////////activity 전환
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        loginUser.setid(id);//현재 로그인 되어있는 유저



        Button searchbutton = (Button) findViewById(R.id.searchbutton);//
        searchbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fromServer = "#123";
                ConnectThread thread = new ConnectThread(1);
                thread.start();//서버와 통신
                while (fromServer == "#123") ;//sleep

                try {//서버로부터 받은 값 자르기
                    jsonObjectIn = (JSONObject) new JSONTokener(fromServer).nextValue();
                    String type = jsonObjectIn.getString("type");
                    String result = jsonObjectIn.getString("content");

                    if (type.equals("search_RE") && result.equals("fail")) {
                        Toast toast = Toast.makeText(getBaseContext(), "검색 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();

                    } else {//검색결과
                        createList(result);
                    }
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(getBaseContext(), "예기치 못한 에러가 발생했습니다.", Toast.LENGTH_SHORT);
                    toast.show();

                    e.printStackTrace();
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) mainadapter.getItem(position);
                String[] curData = curItem.getData();

                Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

            }

        });
    }

    public void createList(String result) {//강의 목록 뿌려주는 함수
        StringTokenizer st = new StringTokenizer(result, "$$");
        int cnt = Integer.parseInt(st.nextToken());
        int check =0;
        //Lecture l = new Lecture();
        mWebSite.clear();

        mainadapter = new IconTextListAdapter(this);

        Resources res = getResources();
        for (int a = 0; a < cnt; a++) {

           WebSite ws = new WebSite();
            ws.setname(st.nextToken());
            ws.setaddress(st.nextToken());
            check = Integer.parseInt(st.nextToken());
            mWebSite.add(ws);

            if(check == 1)//사용중
                mainadapter.addItem(new IconTextItem(res.getDrawable(R.drawable.checking),ws.getname(), ws.getaddress()));
            else
                mainadapter.addItem(new IconTextItem(null,ws.getname(), ws.getaddress()));
        }

        listView1.setAdapter(mainadapter);
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

              //  searchInput = (EditText) findViewById(R.id.searchInput);

                if (type == 1) {//list요청
                    System.out.println("sent1");
                    obj.put("type", "search_RQ");
                    obj.put("id", loginUser.getid());
                }
                Socket sock = new Socket("172.16.206.74", port);
                OutputStream os = sock.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(obj.toString());
                System.out.println("sent2");

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


        return super.onOptionsItemSelected(item);
    }
}
