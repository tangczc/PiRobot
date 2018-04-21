package smarthome;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder.Callback;
import android.annotation.SuppressLint;
import android.content.Context;   
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;   
import android.graphics.Color;
import android.graphics.Paint;   
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class MySurfaceView extends SurfaceView implements Callback, Runnable{
    //////////////////////////////////////////////
	//private Thread th;
	//private SurfaceHolder sfh;
	//private Canvas canvas;
	//private Paint paint;
	//private boolean flag;
	//固定摇杆背景圆形的X,Y坐标以及半径
	private int RockerCircleX = 75;
	private int RockerCircleY = 250;
	private int RockerCircleR = 50;
	//摇杆的X,Y坐标以及摇杆的半径
	private float SmallRockerCircleX = 75,X_before,Y_before;
	private float SmallRockerCircleY = 250;
	private float SmallRockerCircleR = 20;
	///////////////////////////////////////////////////
	private SurfaceHolder sfh;    
    private Canvas canvas;    
    URL videoUrl;
    private String url;
    HttpURLConnection conn;
    Bitmap bmp;
    private Paint p;      
    InputStream inputstream=null;
    private Bitmap mBitmap;    
    private static int mScreenWidth; 
    private static int mScreenHeight;
    public MySurfaceView(Context context, AttributeSet attrs) {

    	  super(context, attrs);
    	  initialize();
    	  p = new Paint();

    	  p.setAntiAlias(true);
    	  sfh = this.getHolder();
    	  sfh.addCallback(this);
    	  this.setKeepScreenOn(true);
    	  setFocusable(true);
    	  //////////////////////////////
    	  setFocusableInTouchMode(true);
    	  //////////////////////////////
    	  this.getWidth();
    	        this.getHeight();  
    	   }
/////////////////////////////////////////////////////////////////
	/***
	 * 得到两点之间的弧度
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		//得到两点X的距离
		float x = px2 - px1;
		//得到两点Y的距离
		float y = py1 - py2;
		//算出斜边长
		float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
		float cosAngle = x / xie;
		//通过反余弦定理获取到其角度的弧度
		float rad = (float) Math.acos(cosAngle);
		//注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			// 当触屏区域不在活动范围内
			/*if (Math.sqrt(Math.pow((RockerCircleX - (int) event.getX()), 2) + Math.pow((RockerCircleY - (int) event.getY()), 2)) >= RockerCircleR) {
				//得到摇杆与触屏点所形成的角度
				double tempRad = getRad(RockerCircleX, RockerCircleY, event.getX(), event.getY());
				//保证内部小圆运动的长度限制
				getXY(RockerCircleX, RockerCircleY, RockerCircleR, tempRad);
			} else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
				SmallRockerCircleX = (int) event.getX();
				SmallRockerCircleY = (int) event.getY();
			}*/
			
			if (Math.sqrt(Math.pow((RockerCircleX - (int) event.getX()), 2) + Math.pow((RockerCircleY - (int) event.getY()), 2)) <= RockerCircleR) {
				SmallRockerCircleX = (int) event.getX();
				SmallRockerCircleY = (int) event.getY();
				//System.out.println("x:"+SmallRockerCircleX+" y:"+SmallRockerCircleY);
			}
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//当释放按键时摇杆要恢复摇杆的位置为初始位置
			SmallRockerCircleX = 75;
			SmallRockerCircleY = 250;
		}
		if(CarConfig.yaogan_flag){
    	 	if((SmallRockerCircleX<40)&&(X_before>=40)){
    	 		System.out.println("左转");
    	 		CarConfig.mPrintWriterClient.print("A");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleX>110)&&(X_before<=110)){
    	 		System.out.println("右转");
    	 		CarConfig.mPrintWriterClient.print("D");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleX>=40)&&(SmallRockerCircleX<=110)&&((X_before>=110)||(X_before<=40))){
    	 		System.out.println("停止");
    	 		CarConfig.mPrintWriterClient.print("P");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	
    	 	if((SmallRockerCircleY<215)&&(Y_before>=215)){
    	 		System.out.println("前进");
    	 		CarConfig.mPrintWriterClient.print("W");    
    	 		CarConfig.mPrintWriterClient.flush();	
    	 	}
    	 	else if((SmallRockerCircleY>285)&&(Y_before<=285)){
    	 		System.out.println("后退");
    	 		CarConfig.mPrintWriterClient.print("S");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleY>=215)&&(SmallRockerCircleY<=285)&&((Y_before>=285)||(Y_before<=215))){
    	 		System.out.println("停止");
    	 		CarConfig.mPrintWriterClient.print("P");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	X_before = SmallRockerCircleX;
    	 	Y_before = SmallRockerCircleY;
		}
		return true;
	}

	/**
	 * 
	 * @param R
	 *            圆周运动的旋转点
	 * @param centerX
	 *            旋转点X
	 * @param centerY
	 *            旋转点Y
	 * @param rad
	 *            旋转的弧度
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		//获取圆周运动的X坐标 
		SmallRockerCircleX = (float) (R * Math.cos(rad)) + centerX;
		//获取圆周运动的Y坐标
		SmallRockerCircleY = (float) (R * Math.sin(rad)) + centerY;
		//System.out.println("x:"+SmallRockerCircleX+" y:"+SmallRockerCircleY);
	}
/////////////////////////////////////////////////////////////////////////
    	 private void initialize() { 
    	          DisplayMetrics dm = getResources().getDisplayMetrics(); 
    	           mScreenWidth = dm.widthPixels; 
    	         mScreenHeight = dm.heightPixels;                  
    	       } 
    	 class DrawVideo extends Thread
    	  {
    	  
    	    public DrawVideo() { 
    	    	
    	   
    	       }     
    	    @SuppressLint("NewApi")
			public void run() {
    		     while (true)
    		     {
    		     try {  
    		     videoUrl=new URL(url);
    		    conn = (HttpURLConnection)videoUrl.openConnection();
    		        conn.connect();
    		          inputstream = conn.getInputStream(); //获取流
    		          bmp = BitmapFactory.decodeStream(inputstream);//从获取的流中构建出BMP图像
    		         mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
    		         /////////////////////////////////////////////////////
    		         if(CarConfig.cam_flag){
    		        	 CarConfig.cam_flag = false;
    		        	 try {
    		        		 	Time time = new Time("GMT+8");       
    		        	        time.setToNow();      
    		        	        int year = time.year;      
    		        	        int month = time.month;      
    		        	        int day = time.monthDay;      
    		        	        int minute = time.minute;      
    		        	        int hour = time.hour;      
    		        	        int sec = time.second;   
    		        	        hour = hour + 8;
    		        	        if(hour>=24){
    		        	        	hour = hour - 24;
    		        	        }
    		        	       FileOutputStream out = new FileOutputStream("/mnt/sdcard/"+year+month+day+hour+minute+sec+".png");
    		        	       bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
    		        	} catch (Exception e) {
    		        	       e.printStackTrace();
    		        	} 
    		         }
    		         /////////////////////////////////////////////////////
    		             canvas = sfh.lockCanvas();
    		             canvas.drawColor(Color.WHITE);
    		             canvas.drawBitmap(mBitmap, 0,0, null);//把BMP图像画在画布上
    		             //////////////////////////////////
    		             if(CarConfig.yaogan_flag){
    		            	 p.setColor(0x70000000);
    		            	 //绘制摇杆背景
    		            	 canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, p);
    		            	 p.setColor(0x70ff0000);
    		            	 //绘制摇杆
    		            	 canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, p);
    		            	 	if((SmallRockerCircleX<40)&&(X_before>=40)){
    		            	 		System.out.println("左转");
    		            	 		CarConfig.mPrintWriterClient.print("A");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleX>110)&&(X_before<=110)){
    		            	 		System.out.println("右转");
    		            	 		CarConfig.mPrintWriterClient.print("D");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleX>=40)&&(SmallRockerCircleX<=110)&&((X_before>=110)||(X_before<=40))){
    		            	 		System.out.println("停止");
    		            	 		CarConfig.mPrintWriterClient.print("P");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	
    		            	 	if((SmallRockerCircleY<215)&&(Y_before>=215)){
    		            	 		System.out.println("前进");
    		            	 		CarConfig.mPrintWriterClient.print("W");    
    		            	 		CarConfig.mPrintWriterClient.flush();	
    		            	 	}
    		            	 	else if((SmallRockerCircleY>285)&&(Y_before<=285)){
    		            	 		System.out.println("后退");
    		            	 		CarConfig.mPrintWriterClient.print("S");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleY>=215)&&(SmallRockerCircleY<=285)&&((Y_before>=285)||(Y_before<=215))){
    		            	 		System.out.println("停止");
    		            	 		CarConfig.mPrintWriterClient.print("P");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	X_before = SmallRockerCircleX;
    		            	 	Y_before = SmallRockerCircleY;
    		             }
    					///////////////////////////////////////////
    		             sfh.unlockCanvasAndPost(canvas);//画完一副图像，解锁画布 
    		         } catch (Exception ex) { 
    		          ex.printStackTrace();
    		         } finally {     
    		         }   
    		     }
    		     }
 
    }    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
    }    
    public void surfaceDestroyed(SurfaceHolder holder) {    
    }   
    public void GetCameraIP(String p){url=p;}

    public void run() {
	     while (CarConfig.sur_flag)
	     {
	     try {  
	     videoUrl=new URL(url);
	    conn = (HttpURLConnection)videoUrl.openConnection();
	        conn.connect();
	          inputstream = conn.getInputStream(); //获取流
	          bmp = BitmapFactory.decodeStream(inputstream);//从获取的流中构建出BMP图像
	         mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
	             canvas = sfh.lockCanvas();
	             canvas.drawColor(Color.WHITE);
	             canvas.drawBitmap(mBitmap, 0,0, null);//把BMP图像画在画布上
	             sfh.unlockCanvasAndPost(canvas);//画完一副图像，解锁画布 
	         } catch (Exception ex) { 
	          ex.printStackTrace();
	         } finally {     
	         }   
	     }
	     }

	public void surfaceCreated(SurfaceHolder holder) {
		new DrawVideo().start();
		
	}
}    