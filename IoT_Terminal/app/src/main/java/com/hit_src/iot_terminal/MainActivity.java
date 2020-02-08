package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit_src.ui.OverviewActivity;

import java.security.KeyStore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //此处代码为开始界面代码，按需修改即可
        Button enterButton=findViewById(R.id.MainActivity_Entersystem_Button);
        enterButton.setOnClickListener(new enterButton_OnClickListener(this));







    }
}

//进入系统按钮的监听器实现
class enterButton_OnClickListener implements View.OnClickListener {
    private Activity self=null;
    enterButton_OnClickListener(Activity parentActivity){
        self=parentActivity;
    }
    @Override
    public void onClick(View v) {
        SystemInit();
    }
    private void SystemInit(){  //系统初始化函数
        //检查状态配置文件
        SharedPreferences sharedPreferences=self.getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
        if(!sharedPreferences.contains("inited")){//如果未初始化
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("inited", true);
            editor.putString("serial", self.getString(R.string.Serialstatus_allbroken));
            editor.putString("internet",self.getString(R.string.Internetstatus_broken));
            editor.commit();
        }
        //启动界面Acitivity
        Intent overviewActivityIntent =new Intent(self, OverviewActivity.class);
        self.startActivity(overviewActivityIntent);

        //系统初始化完毕后，关掉初始化用的Activity
        self.finish();
    }
}