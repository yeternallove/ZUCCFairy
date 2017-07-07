package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在oncreate（）方法中创建了adapter对象，并将adapter作为适配器传递给了listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button submit = (Button)findViewById(R.id.helpSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"Accepted!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(HelpActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
