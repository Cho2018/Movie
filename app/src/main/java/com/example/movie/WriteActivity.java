package com.example.movie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class WriteActivity extends AppCompatActivity {
    TextView movieName;
    RatingBar rbar;
    DatePicker dp;
    EditText edtMemo;
    Button btnWrite, btnHome, btnMake;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setTitle("Movie");

        Intent intent = getIntent();
        String name[] = intent.getStringArrayExtra("ImgName");
        int nameIndex = intent.getIntExtra("ImgNameIndex", 0);

        movieName = (TextView) findViewById(R.id.movieName);
        movieName.setText(name[nameIndex]);

        rbar = (RatingBar) findViewById(R.id.rbar);

        dp = (DatePicker) findViewById(R.id.datePicker);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "_"
                        + Integer.toString(monthOfYear + 1) + "_"
                        + Integer.toString(dayOfMonth) + ".txt";

                String str = readMemo(fileName);
                edtMemo.setText(str);
                btnWrite.setEnabled(true);
            }
        });

        edtMemo = (EditText) findViewById(R.id.edtMemo);

        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str = edtMemo.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName + "이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMake = (Button) findViewById(R.id.btnMake);
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeActivity.class);
                startActivity(intent);
            }
        });
    }

    String readMemo(String fName) {
        String memo = null;
        FileInputStream inFs;

        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            memo = (new String(txt)).trim();
            btnWrite.setText("수정하기");
        } catch (IOException e) {
            edtMemo.setHint("감상평 없음");
            btnWrite.setText("저장하기");
        }

        return memo;
    }
}
