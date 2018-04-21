package rootchen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import fufu.smarthome.R;

public class Main extends Activity {
    EditText edAlias,edIP,edPort,edUser,edPW;
    TextView t;
	private ImageButton cam,cam_up,cam_down,cam_left,cam_right;
    URL videoUrl;
    Bitmap bmp;
    private boolean isConnecting = false;
    private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient	= null;
    public static String CameraIp;
    MySurfaceView r;
	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        CarConfig.sur_flag = true;
		mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);		
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        cam= (ImageButton)findViewById(R.id.cam);
        cam.setOnClickListener(camClickListener);
        cam_up= (ImageButton)findViewById(R.id.cam_up);
        cam_up.setOnClickListener(cam_upClickListener);
        cam_down= (ImageButton)findViewById(R.id.cam_down);
        cam_down.setOnClickListener(cam_downClickListener);
        cam_left= (ImageButton)findViewById(R.id.cam_left);
        cam_left.setOnClickListener(cam_leftClickListener);
        cam_right= (ImageButton)findViewById(R.id.cam_right);
        cam_right.setOnClickListener(cam_rightClickListener);
        r=(MySurfaceView)findViewById(R.id.mySurfaceView1);

        CameraIp ="http://192.168.12.1:8080/?action=snapshot";

        	    r.GetCameraIP(CameraIp);
        	    mThreadClient = new Thread(mRunnable);
				mThreadClient.start();
				
				CarConfig.yaogan_flag = false;
		}
    
	private OnClickListener camClickListener = new OnClickListener() {
		public void onClick(View arg0) {	    	
						Toast.makeText(getApplicationContext(), "Capture success, photos saved in /sdcard ��", Toast.LENGTH_LONG).show();
				    	CarConfig.cam_flag = true;
		}
	};
	private OnClickListener cam_upClickListener = new OnClickListener() {
		public void onClick(View arg0) {
			mPrintWriterClient.print("1");
	    	mPrintWriterClient.flush();
		}
	};
	private OnClickListener cam_downClickListener = new OnClickListener() {
		public void onClick(View arg0) {	    	
			mPrintWriterClient.print("5");
	    	mPrintWriterClient.flush();
		}
	};
	private OnClickListener cam_leftClickListener = new OnClickListener() {
		public void onClick(View arg0) {	    	
			mPrintWriterClient.print("3");
	    	mPrintWriterClient.flush();
		}
	};
	private OnClickListener cam_rightClickListener = new OnClickListener() {
		public void onClick(View arg0) {	    	
			mPrintWriterClient.print("4");
	    	mPrintWriterClient.flush();
		}
	};
	
	private Runnable	mRunnable	= new Runnable() 
	{
		public void run()
		{
			String msgText ="192.168.12.1:2003";

			int start = msgText.indexOf(":");

			String sIP = msgText.substring(0, start);
			String sPort = msgText.substring(start+1);
			int port = Integer.parseInt(sPort);				
			
			Log.d("gjz", "IP:"+ sIP + ":" + port);		

			try 
			{
				mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
				
				mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
				CarConfig.mPrintWriterClient = mPrintWriterClient;
				isConnecting = true;
				Message msg = new Message();
                msg.what = 1;
				mHandler.sendMessage(msg);
			}
			catch (Exception e) 
			{
				Message msg = new Message();
                msg.what = 1;
				mHandler.sendMessage(msg);
				return;
			}			
		}
	};
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{										
		  @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg)										
		  {											
			  super.handleMessage(msg);			
		  }									
	 };
		
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
		
		@SuppressLint("NewApi")
		@Override
		public void onResume(){
			mSensorManager.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);
			
			super.onResume();
		}
		
		
		@SuppressLint("NewApi")
		@Override
		public void onPause(){
			mSensorManager.unregisterListener(lsn);
			
			super.onPause();
		}
		
		@SuppressLint({ "HandlerLeak", "HandlerLeak", "HandlerLeak", "HandlerLeak", "HandlerLeak" })
		SensorEventListener lsn = new SensorEventListener() {
			
			@SuppressLint({ "NewApi", "NewApi", "NewApi" })
			public void onSensorChanged(SensorEvent event) {
			}
			
			@SuppressLint("NewApi")
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
}


