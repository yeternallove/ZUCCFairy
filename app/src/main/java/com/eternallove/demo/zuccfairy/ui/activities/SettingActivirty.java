package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivirty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在oncreate（）方法中创建了adapter对象，并将adapter作为适配器传递给了listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_setting);

        final Context context = this;

        final EditText editNickName = (EditText) findViewById(R.id.editNickName);
        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editBirthday = (EditText) findViewById(R.id.editBirthday);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final EditText editAddress = (EditText) findViewById(R.id.editAddress);

        Button ensure = (Button) findViewById(R.id.setting_ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                sdf.setLenient(false);
                try {
                    sdf.parse(editBirthday.getText().toString());
                } catch (ParseException e) {
                    Toast.makeText(context, "生日格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                String check = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(editEmail.getText().toString());
                if (!matcher.matches()) {
                    Toast.makeText(context, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(SettingActivirty.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button reset = (Button) findViewById(R.id.setting_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNickName.setText("");
                editName.setText("");
                editBirthday.setText("");
                editEmail.setText("");
                editPhone.setText("");
                editAddress.setText("");
            }
        });
    }
}
