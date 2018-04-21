package smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import fufu.smarthome.R;

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button btn_video = (Button) findViewById(R.id.button_video);
        Button btn_control=(Button) findViewById(R.id.button_control);
        Button btn_te = (Button)findViewById(R.id.button_temperature);
        btn_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_control.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Control_Activity.class);
                startActivity(intent);
            }
        });
        btn_te.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,Show_Activity.class);
                startActivity(intent);
            }
        });
    }
}
