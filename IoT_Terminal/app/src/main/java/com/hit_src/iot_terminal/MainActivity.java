package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit_src.iot_terminal.ui.OverviewActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton=findViewById(R.id.MainActivity_Entersystem_Button);
        enterButton.setOnClickListener(new enterButton_OnClickListener(this));
    }
}

class enterButton_OnClickListener implements View.OnClickListener {
    private Activity self;
    enterButton_OnClickListener(Activity parentActivity){
        self=parentActivity;
    }
    @Override
    public void onClick(View v) {
        SystemInit();
    }
    @SuppressLint("ApplySharedPref")
    private void SystemInit(){
        SharedPreferences sharedPreferences=self.getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);

        if(!sharedPreferences.contains("inited")){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("inited", true);
            editor.putString("serial", self.getString(R.string.Serialstatus_allbroken));
            editor.putString("internet",self.getString(R.string.Internetstatus_broken));
            editor.commit();
        }
        Intent overviewActivityIntent =new Intent(self, OverviewActivity.class);
        self.startActivity(overviewActivityIntent);

        self.finish();
    }
}