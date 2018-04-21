package smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import fufu.smarthome.R;
public class  Control_Activity extends Activity {
        private  TcpClientConnector connector;
        Button btn_left;
        Button btn_up;
        Button btn_right;
        Button btn_down;
        Button btn_stop;
        ToggleButton tglbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control);
        btn_up = (Button)findViewById(R.id.forward);
        btn_up.setOnClickListener(new ButtonEvent());
        btn_left = (Button) findViewById(R.id.left);
        btn_left.setOnClickListener(new ButtonEvent());
        btn_right = (Button)findViewById(R.id.right);
        btn_right.setOnClickListener(new ButtonEvent());
        btn_down = (Button)findViewById(R.id.down);
        btn_down.setOnClickListener(new ButtonEvent());
        btn_stop = (Button) findViewById(R.id.stop);
        btn_stop.setOnClickListener(new ButtonEvent());
        tglbtn = (ToggleButton) findViewById(R.id.tglBtn);
        tglbtn.setOnCheckedChangeListener(new TglBtnCheckedChangeEvents());
        connector = TcpClientConnector.getInstance();
    }

      private class TglBtnCheckedChangeEvents implements ToggleButton.OnCheckedChangeListener {
       public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
           if(compoundButton == tglbtn ){
                if (b == true){
                    connector.createConnect("192.168.12.1",2333);
                }else{
                    try {
                        connector.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           }
       }
   }

    private class ButtonEvent implements View.OnClickListener{
        char[] data = new char[512];
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.forward:
                    try {
                        connector.send("0x01");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.left:
                    try {
                        connector.send("0x03");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.right:
                    try {
                        connector.send("0x04");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.down:
                    try {
                        connector.send("0x02");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.stop:
                        try{
                        connector.send("0x00");
                        }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}

