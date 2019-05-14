package com.wh2per.human_pose_estimation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startbutton;
    private Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cont = getApplicationContext();

        startbutton = findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, WebViewActivity.class);
                startActivity(intent);
                Log.e("a","aaa");
            }
        });
    }
}