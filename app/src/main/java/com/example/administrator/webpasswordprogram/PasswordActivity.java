package com.example.administrator.webpasswordprogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PasswordActivity extends AppCompatActivity {
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_ERROR = 400;

    TextView siteName;
    TextView password;

    JSONObject obj;
    JSONObject jsonObjectIn;//서버로부터 받은 data json으로
    String fromServer;//서버로부터 받은 data

    User curUser = new User();//회원가입할 유저

    public Key generateKey(String algorithm, byte[] keyData) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {

        String upper = algorithm.toUpperCase();

        if ("DES".equals(upper)) {
            KeySpec keySpec = new DESKeySpec(keyData);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            return secretKey;

        } else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
            KeySpec keySpec = new DESedeKeySpec(keyData);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            return secretKey;
        } else {
            SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
            return keySpec;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ////////////////////////////////activity 전환
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        curUser.setid(id);//현재 로그인 되어있는 유저

        siteName = (TextView) findViewById(R.id.siteNameLabel);
        password= (TextView) findViewById(R.id.passwordViewLabel);


        fromServer = "#123";
        ConnectThread thread = new ConnectThread(1);
        thread.start();//서버와 통신
        while (fromServer == "#123") ;//sleep

        try {//서버로부터 받은 값 자르기
            jsonObjectIn = (JSONObject) new JSONTokener(fromServer).nextValue();
            String type = jsonObjectIn.getString("type");
            String result = jsonObjectIn.getString("content");

            if (type.equals("password_RE") && result.equals("fail")) {
                Toast toast = Toast.makeText(getBaseContext(), "요청하신 비밀번호가 없습니다.", Toast.LENGTH_SHORT);
                toast.show();

            } else {//검색결과
                StringTokenizer st = new StringTokenizer(result, "$$");

                siteName.setText(st.nextToken());
               // password.setText(st.nextToken());
                String str_Password = st.nextToken();   // 스트링에 서버에서 받은 패스워드 저장
                byte[] byte_SessionKey = curUser.getsession_key().getBytes(); // 세션키를 바이트로 변환
                String transformation = "DES/ECB/NoPadding";
                try {
                    Cipher cipher = Cipher.getInstance(transformation);

                    Key newKey = generateKey("DES", byte_SessionKey);   //변환한 바이트로 키 생성
                    byte[] byte_Password = str_Password.getBytes();

                    cipher.init(Cipher.DECRYPT_MODE, newKey);
                    byte[] decrypt = cipher.doFinal(byte_Password);
                    String byteToString = new String(byte_Password, "UTF-8");   // 사이트에 입력할 비밀번호
                    password.setText(byteToString); // 출력
                }catch (NoSuchAlgorithmException e){
                }catch (NoSuchPaddingException e){
                }catch (InvalidKeySpecException e){
                }catch (InvalidKeyException e){
                }catch (IllegalBlockSizeException e){
                }catch (BadPaddingException e){
                }catch (UnsupportedEncodingException e){
                }

            }
        } catch (JSONException e) {
            Toast toast = Toast.makeText(getBaseContext(), "예기치 못한 에러가 발생했습니다.", Toast.LENGTH_SHORT);
            toast.show();

            e.printStackTrace();
        }



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

                if (type == 1) {//비밀번호 요청

                    obj.put("type", "password_RQ");
                    obj.put("id", curUser.getid());
                }

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
