package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.ui.OverviewActivity;

import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    private Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self=this;
        Button enterButton=findViewById(R.id.MainActivity_Entersystem_Button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(v.getContext());

                Intent overviewActivityIntent =new Intent(self, OverviewActivity.class);
                overviewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overviewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.startActivity(overviewActivityIntent);

                self.finish();
            }
        });
    }
}
