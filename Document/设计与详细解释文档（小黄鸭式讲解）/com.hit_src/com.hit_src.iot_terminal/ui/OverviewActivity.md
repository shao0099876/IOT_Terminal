# OverviewActivity代码解释

```
private static final int SERIAL_UPDATE=1;
private static final int INTERNET_UPDATE=2;
private static final int OVERVIEW_UPDATE=3;
```

定义3个常量，用于在后面向Handler通过Message发送消息时确定消息值，分别代表传感状态更新消息，互联网状态更新消息，综合状态更新消息。

```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        uiStatusUpdate(SERIAL_UPDATE);
        uiStatusUpdate(INTERNET_UPDATE);
        uiStatusUpdate(OVERVIEW_UPDATE);
        SharedPreferences sp=getSharedPreferences("StatusProfile", Activity.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(profileChangeListener);
    }
```

onCreate函数，在完成setContentView绘图工作后，立即执行uiStatusUpdate函数，对3个状态执行绘图更新操作。完成后对状态文件StatusProfile注册监听器profileChangeListener

```
    private SharedPreferences.OnSharedPreferenceChangeListener profileChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("serial")){
                uiStatusUpdate(SERIAL_UPDATE);
            }
            else if(key.equals("internet")){
                uiStatusUpdate(INTERNET_UPDATE);
            }
            uiStatusUpdate(OVERVIEW_UPDATE);
        }
    };
```

定义profileChangeListener
- 在复写函数onSharedPreferenceChanged时，__猜测__ 参数1为发生改变的SharedPreferences，参数2为发生改变的key
- __绝对不可使用switch处理String类型数据__
- 不论谁发生了改变，最终都要执行Overview的更新操作

```
    private void uiStatusUpdate(final int code){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Message mes=new Message();
                mes.what=code;
                handler.sendMessage(mes);
            }
        });
        t.start();
    }
```

定义uiStatusUpdate函数
- 该函数启动一个线程来对ui界面控件进行更新，由于Android固有的问题，这个线程内刷新必须要通过Handler进行，因此通过Message将刷新类型传递给handler

```
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            String status;
            SharedPreferences sp;
            switch (msg.what){
                case SERIAL_UPDATE: //serial status update
                    sp =getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    status=sp.getString("serial","ERROR!");
                    if(status.equals(getString(R.string.Serialstatus_allbroken))){
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_allbroken);
                    }
                    else if(status.equals(getString(R.string.Serialstatus_partbroken))) {
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.yellowlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_partbroken);
                    }
                    else if(status.equals(getString(R.string.Serialstatus_normal))) {
                        ImageView imageView = findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView = findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_normal);
                    }
                    break;
                case INTERNET_UPDATE:
                    sp=getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    status=sp.getString("internet","ERROR!");
                    if(status.equals(getString(R.string.Internetstatus_broken))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_broken);
                    }
                    else if(status.equals(getString(R.string.Internetstatus_normal))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_normal);
                    }
                    break;
                case OVERVIEW_UPDATE:
                    TextView textView1=findViewById(R.id.Overview_Internetstatus_Textview);
                    TextView textView2= findViewById(R.id.Overview_Serialstatus_Textview);
                    ImageView imageView=findViewById(R.id.Overview_Overviewstatus_ImageView);
                    TextView textView=findViewById(R.id.Overview_OverviewStatus_TextView);
                    int cnt=0;
                    if(textView1.getText().equals(getString(R.string.Internetstatus_normal))){
                        cnt+=1;
                    }
                    if(textView2.getText().equals(getString(R.string.Serialstatus_normal))){
                        cnt+=1;
                    }
                    switch(cnt){
                        case 0:
                            imageView.setImageResource(R.drawable.redlight);
                            textView.setText(R.string.Overview_allbroken);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.yellowlight);
                            textView.setText(R.string.Overview_partbroken);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.greenlight);
                            textView.setText(R.string.Overview_normal);
                            break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
```

定义Handler
- 主要复写里面的handlerMessage，通过解析msg参数的what属性，可以获得之前uiStatusUpdate函数通过消息传递的值
- 解析值时，对于串口类型界面刷新，要读状态配置文件中的serial字段，如果allbroken，则更新Textview，图片放红灯；如果partbroken，更新Textview，图片放黄灯；如果normal，更新Textview，图片放绿灯。对于互联网类型界面刷新，读状态配置文件中的internet字段，根据broken或normal，分别放红灯或绿灯。对于综合类型界面刷新，读状态配置文件中的serial和internet字段，统计两个字段中，normal状态的数量，0放红灯，1放黄灯，2放绿灯。
- 不要忘了Handler是通过super.handleMessage(msg)来处理那个消息的
- Message消息的传递是通过消息队列进行，Handler本质是监听、接收、处理消息，因此过程是：接收消息->解析消息->处理消息->调用super.handleMessage以结束处理。