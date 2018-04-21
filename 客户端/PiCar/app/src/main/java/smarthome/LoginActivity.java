//登录界面，此界面可选择进入短信监控模式或wifi监控模式

package smarthome;

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

public class LoginActivity extends Activity {
	
	//开发板TCP服务器IP
	String ip;
	//开发板TCP服务器监听的端口号
	int port;

	//TCP服务器连接IP、端口号编辑框
	private EditText ipedittext;
	private EditText portedittext;
	

	private Button wifi_button;


	//安卓手机客户端tcp套接字
	private Socket socket;

	// 进度条,用来显示wifi连接状态
	private ProgressDialog progressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//隐藏标题栏、状态栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DisplayMetrics displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);

		setContentView(R.layout.login);
		
		//用户输入的端口和IP编辑框
		ipedittext = (EditText)findViewById(R.id.editText2);
		portedittext = (EditText)findViewById(R.id.editText1);

	
		
		wifi_button = (Button) findViewById(R.id.button);
		wifi_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent it = new Intent(LoginActivity.this,Main.class);
				LoginActivity.this.startActivity(it);

			}
		});
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	//进度条,用于显示wifi的连接状态
	@SuppressLint({ "HandlerLeak", "HandlerLeak", "HandlerLeak" })
	private Handler handler = new Handler() {

		@SuppressLint({ "HandlerLeak", "HandlerLeak" })
		public void handleMessage(Message message) {
			switch (message.what) {
			case 1:
				// 刷新UI，显示数据，并关闭进度条
				progressDialog.dismiss(); // 关闭进度条
				break;
			}
		}
	};

}
