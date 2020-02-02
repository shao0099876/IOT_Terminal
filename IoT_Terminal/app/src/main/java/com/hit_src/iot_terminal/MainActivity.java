package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit_src.ui.MainuiActivity;

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
        //启动界面Acitivity
        Intent mainuiActivityIntent =new Intent(self, MainuiActivity.class);
        self.startActivity(mainuiActivityIntent);

        //系统初始化完毕后，关掉初始化用的Activity
        self.finish();
    }
}