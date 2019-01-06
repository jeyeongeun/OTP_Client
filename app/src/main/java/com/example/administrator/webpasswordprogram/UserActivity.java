package com.example.administrator.webpasswordprogram;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UserActivity extends AppCompatActivity {
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_ERROR = 400;

    EditText idInput;
    EditText pwInput;
    EditText nameInput;
    EditText emailInput;
    EditText gradeInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        idInput = (EditText) findViewById(R.id.idInput);
        pwInput= (EditText) findViewById(R.id.pwInput);
        nameInput= (EditText) findViewById(R.id.nameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String idStr =idInput.getText().toString();
                String pwStr =pwInput.getText().toString();
                String nameStr =nameInput.getText().toString();
                String emailStr = emailInput.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", idStr);
                resultIntent.putExtra("pw", pwStr);
                resultIntent.putExtra("name", nameStr);
                resultIntent.putExtra("email", emailStr);

                setResult(RESPONSE_CODE_OK, resultIntent);
                finish();
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
