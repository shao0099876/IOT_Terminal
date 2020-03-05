package com.hit_src.iot_terminal.ui.component.button.serial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.SerialActivity;
import com.hit_src.iot_terminal.ui.handler.SerialUIHandler;

@SuppressLint("AppCompatCustomView")
public class SerialEditButton extends Button {
    private Activity self;
    public SerialEditButton(Context context) {
        super(context);
        self= (Activity) context;
        addListener();
    }

    public SerialEditButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        self= (Activity) context;
        addListener();
    }

    public SerialEditButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self= (Activity) context;
        addListener();
    }
    private void addListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=self.findViewById(R.id.Serial_edit_ID_editText);
                Spinner spinner=self.findViewById(R.id.Serial_edit_type_Spinner);
                String tmp= String.valueOf(editText.getText());
                if(tmp.isEmpty()){
                    new AlertDialog.Builder(self).setTitle("警告").setMessage("未选中要编辑的传感器").show();
                }
                else{
                    int id=Integer.parseInt(tmp);
                    int typenum=spinner.getSelectedItemPosition();
                    Database.exec(Database.UPDATE_SENSOR,new Object[]{id,typenum});
                    MessageThread.sendMessage(((SerialActivity)self).handler, SerialUIHandler.LIST_FLUSH);
                    MessageThread.sendMessage(((SerialActivity)self).handler, SerialUIHandler.EDITAREA_CLEAR);
                }
            }
        });
    }

}
