package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.service.internet.InternetService;
import com.hit_src.iot_terminal.ui.OverviewActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * MainActivity 是系统启动界面
     * enterButton中注册的函数就是系统初始化所需要的的步骤
     */
    private MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self=this;
        //test code
        //!test code
        Button enterButton=findViewById(R.id.MainActivity_Entersystem_Button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(self);//初始化数据库
                new InternetSettings(self);//初始化网络设置文件

                //启动Service
                self.startService(new Intent(self, InternetService.class));
                //启动OverviewActivity
                Intent overviewActivityIntent =new Intent(self, OverviewActivity.class);
                overviewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overviewActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.startActivity(overviewActivityIntent);

                self.finish();//MainActivity使命完成，永久关闭
            }
        });
    }
}
