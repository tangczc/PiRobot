package rootchen;


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
	private int RockerCircleX = 75;
	private int RockerCircleY = 250;
	private int RockerCircleR = 50;
	private float SmallRockerCircleX = 75,X_before,Y_before;
	private float SmallRockerCircleY = 250;
	private float SmallRockerCircleR = 20;
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
    	  setFocusableInTouchMode(true);
    	  this.getWidth();
    	        this.getHeight();  
    	   }
	public double getRad(float px1, float py1, float px2, float py2) {
		float x = px2 - px1;
		float y = py1 - py2;
		float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		float cosAngle = x / xie;
		float rad = (float) Math.acos(cosAngle);
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			
			if (Math.sqrt(Math.pow((RockerCircleX - (int) event.getX()), 2) + Math.pow((RockerCircleY - (int) event.getY()), 2)) <= RockerCircleR) {
				SmallRockerCircleX = (int) event.getX();
				SmallRockerCircleY = (int) event.getY();
			}
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			SmallRockerCircleX = 75;
			SmallRockerCircleY = 250;
		}
		if(CarConfig.yaogan_flag){
    	 	if((SmallRockerCircleX<40)&&(X_before>=40)){
    	 		CarConfig.mPrintWriterClient.print("");
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleX>110)&&(X_before<=110)){
    	 		CarConfig.mPrintWriterClient.print("D");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleX>=40)&&(SmallRockerCircleX<=110)&&((X_before>=110)||(X_before<=40))){
    	 		System.out.println("ֹͣ");
    	 		CarConfig.mPrintWriterClient.print("P");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	
    	 	if((SmallRockerCircleY<215)&&(Y_before>=215)){
    	 		CarConfig.mPrintWriterClient.print("W");    
    	 		CarConfig.mPrintWriterClient.flush();	
    	 	}
    	 	else if((SmallRockerCircleY>285)&&(Y_before<=285)){
    	 		CarConfig.mPrintWriterClient.print("S");    
    	 		CarConfig.mPrintWriterClient.flush();
    	 	}
    	 	else if((SmallRockerCircleY>=215)&&(SmallRockerCircleY<=285)&&((Y_before>=285)||(Y_before<=215))){
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
	 *            Բ���˶�����ת��
	 * @param centerX
	 *            ��ת��X
	 * @param centerY
	 *            ��ת��Y
	 * @param rad
	 *            ��ת�Ļ���
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		//��ȡԲ���˶���X���� 
		SmallRockerCircleX = (float) (R * Math.cos(rad)) + centerX;
		//��ȡԲ���˶���Y����
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
    		          inputstream = conn.getInputStream(); //��ȡ��
    		          bmp = BitmapFactory.decodeStream(inputstream);//�ӻ�ȡ�����й�����BMPͼ��
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
    		             canvas.drawBitmap(mBitmap, 0,0, null);//��BMPͼ���ڻ�����
    		             //////////////////////////////////
    		             if(CarConfig.yaogan_flag){
    		            	 p.setColor(0x70000000);
    		            	 //����ҡ�˱���
    		            	 canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, p);
    		            	 p.setColor(0x70ff0000);
    		            	 //����ҡ��
    		            	 canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, p);
    		            	 	if((SmallRockerCircleX<40)&&(X_before>=40)){
    		            	 		System.out.println("��ת");
    		            	 		CarConfig.mPrintWriterClient.print("A");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleX>110)&&(X_before<=110)){
    		            	 		System.out.println("��ת");
    		            	 		CarConfig.mPrintWriterClient.print("D");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleX>=40)&&(SmallRockerCircleX<=110)&&((X_before>=110)||(X_before<=40))){
    		            	 		System.out.println("ֹͣ");
    		            	 		CarConfig.mPrintWriterClient.print("P");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	
    		            	 	if((SmallRockerCircleY<215)&&(Y_before>=215)){
    		            	 		System.out.println("ǰ��");
    		            	 		CarConfig.mPrintWriterClient.print("W");    
    		            	 		CarConfig.mPrintWriterClient.flush();	
    		            	 	}
    		            	 	else if((SmallRockerCircleY>285)&&(Y_before<=285)){
    		            	 		System.out.println("����");
    		            	 		CarConfig.mPrintWriterClient.print("S");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	else if((SmallRockerCircleY>=215)&&(SmallRockerCircleY<=285)&&((Y_before>=285)||(Y_before<=215))){
    		            	 		System.out.println("ֹͣ");
    		            	 		CarConfig.mPrintWriterClient.print("P");    
    		            	 		CarConfig.mPrintWriterClient.flush();
    		            	 	}
    		            	 	X_before = SmallRockerCircleX;
    		            	 	Y_before = SmallRockerCircleY;
    		             }
    					///////////////////////////////////////////
    		             sfh.unlockCanvasAndPost(canvas);//����һ��ͼ�񣬽������� 
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
	          inputstream = conn.getInputStream(); //��ȡ��
	          bmp = BitmapFactory.decodeStream(inputstream);//�ӻ�ȡ�����й�����BMPͼ��
	         mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
	             canvas = sfh.lockCanvas();
	             canvas.drawColor(Color.WHITE);
	             canvas.drawBitmap(mBitmap, 0,0, null);//��BMPͼ���ڻ�����
	             sfh.unlockCanvasAndPost(canvas);//����һ��ͼ�񣬽������� 
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