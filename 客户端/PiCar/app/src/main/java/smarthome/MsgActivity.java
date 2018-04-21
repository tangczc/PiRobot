//短信监控界面，此界面一个按钮对应一条信息，获取车辆状态信息后在ui上显示

package smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import fufu.smarthome.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.BroadcastReceiver;

public class MsgActivity extends Activity {
	
	//设置广播监听系统短信
    BroadcastReceiver rcvMsgSent = null;
    BroadcastReceiver rcvMsgReceipt = null;
    BroadcastReceiver rcvIncoming = null;
    
    private boolean isConnecting = false;
    private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient	= null;
    
    //短信内容
    public String message;
	
	@SuppressLint({ "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//隐藏标题栏、状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,                
                WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		
		this.setContentView(R.layout.msg);
		
		mThreadClient = new Thread(mRunnable);
		mThreadClient.start();
		
		//出门模式
		final ImageButton out_on = (ImageButton) findViewById(R.id.out_on);
		out_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("1");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Set Out mode",Toast.LENGTH_SHORT).show();
				}
		});
		
		//居家模式
		final ImageButton out_off = (ImageButton) findViewById(R.id.out_off);
		out_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("0");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Set Home mode",Toast.LENGTH_SHORT).show();
				}
		});
		
		//开家门
		final ImageButton door_on = (ImageButton) findViewById(R.id.door_on);
		door_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("3");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Open Door",Toast.LENGTH_SHORT).show();
				}
		});
		
		//关家门
		final ImageButton door_off = (ImageButton) findViewById(R.id.door_off);
		door_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("2");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Close Door",Toast.LENGTH_SHORT).show();
				}
		});
		
		//开空调
		final ImageButton air_on = (ImageButton) findViewById(R.id.air_on);
		air_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("5");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Open Air conditioning",Toast.LENGTH_SHORT).show();
		}
		});
		
		//关空调
		final ImageButton air_off = (ImageButton) findViewById(R.id.air_off);
		air_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("4");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Close Air conditioning",Toast.LENGTH_SHORT).show();}
		});
		
		//开插座A
		final ImageButton switcha_on = (ImageButton) findViewById(R.id.switcha_on);
		switcha_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("7");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Open Switch A",Toast.LENGTH_SHORT).show();
		}
		});
		
		//关插座A
		final ImageButton switcha_off = (ImageButton) findViewById(R.id.switcha_off);
		switcha_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("6");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Close Switch A",Toast.LENGTH_SHORT).show();	
		}
		});
		
		//开插座B
		final ImageButton switchb_on = (ImageButton) findViewById(R.id.switchb_on);
		switchb_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("9");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Open Switch B",Toast.LENGTH_SHORT).show();
}
		});
		
		//关插座B
		final ImageButton switchb_off = (ImageButton) findViewById(R.id.switchb_off);
		switchb_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPrintWriterClient.print("8");
		    	mPrintWriterClient.flush();
				Toast.makeText(MsgActivity.this,"Close Switch B",Toast.LENGTH_SHORT).show();
				}
		});
	    
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		onPause();
	}
	
	public void onDestroy() {
		super.onDestroy();
		if (isConnecting) 
		{				
			isConnecting = false;
			try {
				if(mSocketClient!=null)
				{
					mSocketClient.close();
					mSocketClient = null;
					
					mPrintWriterClient.close();
					mPrintWriterClient = null;
					CarConfig.sur_flag = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mThreadClient.interrupt();
		}

	}
	
	private Runnable	mRunnable	= new Runnable() 
	{
		public void run()
		{
			String msgText ="192.168.1.1:2001";

			int start = msgText.indexOf(":");

			String sIP = msgText.substring(0, start);
			String sPort = msgText.substring(start+1);
			int port = Integer.parseInt(sPort);				
			
			Log.d("gjz", "IP:"+ sIP + ":" + port);		

			try 
			{				
				//连接服务器
				mSocketClient = new Socket(sIP, port);	//portnum
				//取得输入、输出流
				mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
				
				mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
				CarConfig.mPrintWriterClient = mPrintWriterClient;

				//mPrintWriterClient.print("MOVE");
		    	//mPrintWriterClient.flush();
				isConnecting = true;
	
				//break;
			}
			catch (Exception e) 
			{
				return;
			}			
		}
	};
}
