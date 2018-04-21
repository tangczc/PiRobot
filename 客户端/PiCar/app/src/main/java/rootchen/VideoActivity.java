/**
 * 通过tcp链接服务器获取视频流
 */
package rootchen;

import java.net.Socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import fufu.smarthome.R;

public class VideoActivity extends Activity {

	String ip;
	int port;
	private EditText ipedittext;
	private EditText portedittext;
	private Button wifi_button;
	private Socket socket;
	private ProgressDialog progressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		DisplayMetrics displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		setContentView(R.layout.login);
		ipedittext = (EditText)findViewById(R.id.editText2);
		portedittext = (EditText)findViewById(R.id.editText1);
		wifi_button = (Button) findViewById(R.id.button);
		wifi_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent it = new Intent(VideoActivity.this,Main.class);
				VideoActivity.this.startActivity(it);

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@SuppressLint({ "HandlerLeak", "HandlerLeak", "HandlerLeak" })
	private Handler handler = new Handler() {

		@SuppressLint({ "HandlerLeak", "HandlerLeak" })
		public void handleMessage(Message message) {
			switch (message.what) {
			case 1:
				progressDialog.dismiss();
				break;
			}
		}
	};

}
