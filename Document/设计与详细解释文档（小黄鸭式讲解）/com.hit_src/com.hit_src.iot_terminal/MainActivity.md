# MainActivity代码解释

```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton=findViewById(R.id.MainActivity_Entersystem_Button);
        enterButton.setOnClickListener(new enterButton_OnClickListener(this));
    }
}
```

MainActivity的作用是提供软件启动初始化步骤，在当前的设计中，为毕业设计而设计了软件标题、作者、版本号的信息显示。对于软件而言，其作用是启动初始化步骤。初始化的代码通过enterButton的监听器触发

```
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
        //启动界面Activity
        Intent overviewActivityIntent =new Intent(self, OverviewActivity.class);
        self.startActivity(overviewActivityIntent);

        //系统初始化完毕后，关掉初始化用的Activity
        self.finish();
    }
}
```

该类是一个实现了OnClickListener接口的监听器类，负责处理enterButton被点击时的触发。
- 构造函数需要Activity进行初始化是由于后面的SharedPreferences需要调用Activity的方法来获取，__未知直接调用静态方法是否可行，但Intent启动界面Activity确实需要父Activity的一个实例__
- 状态配置文件的检查需要通过inited标记进行，因为通过getSharedPreferences是不能知道配置文件是否存在于文件系统中。
- 状态配置文件名：StatusProfile。状态配置文件3个key：inited,serial,internet。inited是boolean型；serial是String型，只可能有R.string.Serialstatus_allbroken，……_partbroken，……_normal，共3种可能值；internet是String型，只可能有R.string.Internetstatus_broken，……_normal，共2种可能值。
- 状态配置文件3个key的初始值：inited——true，serial——R.string.Serialstatus_allbroken，internet——R.string.Internetstatus_broken，__至于不检查inited直接写入初始值的方法，要根据后期传感网络、互联网等模块的状态监测框架建立后再斟酌修改__
- 启动界面Activity通过Intent进行，两个参数分别指出父Activity的实例是谁，将要启动的Activity是哪个
- 系统初始化完毕后，通过finish函数关闭启动界面的Activity，以保证此界面只会显示1次，__至于直接finish后Activity并未从内存中删除自己的后果与影响，暂不做考虑__