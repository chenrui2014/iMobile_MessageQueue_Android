package com.supermap.demo.mqdemo;



import java.util.ArrayList;

import com.supermap.data.Environment;

import android.app.Activity;
import android.app.Application;
import android.os.Process;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


public class MyApplication extends Application {
	public static String DATAPATH = "";
	public static String SDCARD   = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	
	private static MyApplication sInstance = null;

	private ArrayList<Activity> mActivities = new ArrayList<Activity>();
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		DATAPATH = this.getFilesDir().getAbsolutePath() + "/";
		sInstance = this;
	
		Environment.setLicensePath(DefaultDataConfiguration.LicensePath);
		com.supermap.messagequeue.Environment.initialization(this);
		Environment.setWebCacheDirectory(DefaultDataConfiguration.WebCachePath);
		Environment.initialization(this);
		
		// Initialization
//		MySharedPreferences.init(this);
		MyAssetManager.init(this);
	}
	
	/**
	 * ��ȡ��ǰӦ�ö���
	 * @return
	 */
	public static MyApplication getInstance()
	{
		return sInstance;
	}
	
	/**
	 * Toast��ʾ��Ϣ
	 * @param info
	 */
	public   void showInfo(String info)
	{
		Toast toast = Toast.makeText(sInstance, info, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
		
	}
	
	/**
	 * Toast��ʾ������Ϣ
	 * @param err
	 */
	public void showError (String err) 
	{
		Toast toast = Toast.makeText(sInstance, "Error: " + err, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
		Log.e(this.getClass().getName(), err);
	}
	
	/**
	 * ��ȡ��ʾ�ߴ�ֵ
	 * @param dp
	 * @return
	 */
	public static int dp2px (int dp) 
	{
		int density = (int) (dp*sInstance.getResources().getDisplayMetrics().density);
		
		return density;
	}
	
	/**
	 * ע��Activity
	 * @param act
	 */
	public void registerActivity(Activity act){
		mActivities.add(act);
	}
	
	/**
	 * �˳�Ӧ��
	 */
	public void exit(){
	
		for(Activity act:mActivities){
			act.finish();
		}
		Process.killProcess(Process.myPid());
	}
}

