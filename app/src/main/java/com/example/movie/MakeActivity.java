package com.example.movie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MakeActivity extends AppCompatActivity {
    MyCanvas canvas;
    String file_name = "sample.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);
        setTitle("Movie");

        canvas = (MyCanvas)findViewById(R.id.canvas);

        Button btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"굵기").setCheckable(true);
        menu.add(1,2,0,"빨강색");
        menu.add(2,3,0,"파랑색");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
            if(item.isChecked()) item.setChecked(false);
            else item.setChecked(true);

            canvas.setPenWidth(item.isChecked());
        }else if(item.getItemId() == 2){
            canvas.setPenColor(true);
        }else if(item.getItemId() == 3){
            canvas.setPenColor(false);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        String tag = (String)v.getTag();

        switch (tag){
            case "save":
                canvas.save(getFilesDir() + file_name);
                break;

            case "open":
                canvas.open(getFilesDir() + file_name, getApplicationContext());
                break;

            case "eraser":
                canvas.setOperationType(tag);
                break;
        }
    }
}
