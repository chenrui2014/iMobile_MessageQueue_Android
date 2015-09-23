package com.supermap.demo.mqdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.plot.GeoGraphicObject;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.mapping.Action;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;

public class PlotTypePopup extends PopupWindow implements OnClickListener {

	private MapControl      m_MapControl       = null;
	
	private LayoutInflater  m_LayoutInflater   = null;
	private View            m_ContentView      = null;
	private MainActivity    m_MainActivity     = null;
	private PlotSymbolPopup m_PlotSymbolPopup  = null;
	
	public int m_queryType = 0;//0����ǰλ�ò�ѯ��1����ѡ�в�ѯ
	public Object m_QueryObject = null;
	
	private View m_MainView = null;
	
	public PlotTypePopup (MapControl mapControl, View mainView, MainActivity activity){
		super(activity);
    	
		m_LayoutInflater = LayoutInflater.from(mapControl.getContext());
		m_MapControl     = mapControl;
		m_MainActivity   = activity;
		m_MainView		 = mainView;
		
		initView();
		
//		//��Ӧ���ؼ�����
		setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		this.setFocusable(false);
	}
	
	private void initView() {
		m_ContentView = m_LayoutInflater.inflate(R.layout.plot_bar, null);
		
		setContentView(m_ContentView);

		((Button) m_ContentView.findViewById(R.id.btn_plot)).setOnClickListener(this);
		((Button) m_ContentView.findViewById(R.id.btn_line)).setOnClickListener(this);
		((Button) m_ContentView.findViewById(R.id.btn_region)).setOnClickListener(this);
		((Button) m_ContentView.findViewById(R.id.btn_scrawl)).setOnClickListener(this);
		((Button) m_ContentView.findViewById(R.id.btn_arrow)).setOnClickListener(this);
		((Button) m_ContentView.findViewById(R.id.btn_clear)).setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_plot:
			m_MapControl.setAction(Action.CREATEPLOT);
			
			String path = DefaultDataConfiguration.MapDataPath + "SymbolIcon/����/";
			
			File file = new File(path);
			
			m_SymbolBmps.clear();
			m_SymbolIDs.clear();
			getFileBitmaps(file);
			
			if(m_PlotSymbolPopup == null){
				m_PlotSymbolPopup = new PlotSymbolPopup(m_MapControl, m_SymbolBmps, m_SymbolIDs, m_MainActivity.getLibIDJB(), m_MainActivity);
			}
			else{
				m_PlotSymbolPopup.resetView(m_SymbolBmps, m_SymbolIDs, m_MainActivity.getLibIDJB());
			}
			m_PlotSymbolPopup.show();

			break;
		case R.id.btn_line:
			
			m_MapControl.setAction(Action.DRAWLINE);
			break;
		case R.id.btn_region:
			m_MapControl.setAction(Action.DRAWPLOYGON);
			break;
		case R.id.btn_scrawl:
			m_MapControl.setAction(Action.FREEDRAW);
			break;
		case R.id.btn_arrow:
			m_MapControl.setAction(Action.CREATEPLOT);

			String path2 = DefaultDataConfiguration.MapDataPath + "SymbolIcon/��ͷ���/";
			
			File file2 = new File(path2);

			m_SymbolBmps.clear();
			m_SymbolIDs.clear();
			getFileBitmaps(file2);

			if(m_PlotSymbolPopup == null){
				m_PlotSymbolPopup = new PlotSymbolPopup(m_MapControl, m_SymbolBmps, m_SymbolIDs, (int)m_MainActivity.getLibIDTY(), m_MainActivity);
			}
			else{
				m_PlotSymbolPopup.resetView(m_SymbolBmps, m_SymbolIDs, (int)m_MainActivity.getLibIDTY());
			}
			m_PlotSymbolPopup.show();
			
			break;
		case R.id.btn_clear:
			//��ȡ��ǰ�༭ͼ��
			Layer layer = m_MapControl.getMap().getLayers().get(0);
			//��ͼ���ȡ�������ݼ��ļ�¼��
			Recordset rc = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
			//�༭ɾ������
			rc.deleteAll();
			//���¼�¼��
			rc.update();
			//ˢ�µ�ͼ��ʾ�������
			m_MapControl.getMap().refresh();
			m_MainActivity.clearLists();
			
			break;
		default:
			break;
		}
		this.dismiss();
	}

	private java.util.Map<String, Bitmap> m_SymbolBmps = new HashMap<String, Bitmap>();
	private ArrayList<String> m_SymbolIDs = new ArrayList<String>();
//	ArrayList<Bitmap> m_SymbolBmps = new ArrayList<Bitmap>();
	private boolean getFileBitmaps(File file){
		
		if(file.exists())
		{
	        if (file.isDirectory()) {
	            File[] fileList = file.listFiles();
	            for (File f : fileList) {
	                getFileBitmaps(f);
	            }
	        } else {
	        	//�Ǵ��ڵ��ļ��Ż�ת������
	        	if(file.isFile()){
	        		try{
		        		InputStream is = new FileInputStream(file);
		        		//�����������õ�ͼƬλͼ
		        		Bitmap bmp = BitmapFactory.decodeStream(is);
		        		
		        		if(bmp != null){
		        			//��ȡ����ͼƬ���ļ�����
		        			String strBmpFullName = file.getName();
		        			String[] strBmpArrayName = strBmpFullName.split("\\.");
		        			
		        			if(strBmpArrayName.length != 2)
		        				return false;
		        			
		        			//�����ʵ�ı����ŵı���
		        			String strBmpName = strBmpArrayName[0];
		        			
		        			//�������ͼƬ���ڴ�ӳ�����
		        			m_SymbolBmps.put(strBmpName, bmp);
		        			m_SymbolIDs.add(strBmpName);

//		        			m_SymbolBmps.add(bmp);
//		        			String bmpCodePath = file.getPath();
//		        			String strBmpName = bmpCodePath.substring(bmpCodePath.length() - bmpCodePath.lastIndexOf("/"));
		        			
		        			return true;
		        		}
	        		}
	        		catch(Exception e){
	        		}
	        	}
	        }
	        
			return false;
		}
		
		return false;
	}
	
	Recordset m_GeoMesRecordset = null;
	/**
	 * ���յ��Ĵ���ӵ����ݿⲢ������ͼ��ʾ
	 * @Parma String
	 */
	private boolean addRecivedGeometry(String geoMsg){
		if(m_GeoMesRecordset == null){
			return false;
		}
		
		//���������Ϣ����Ϊ���ζ���
		GeoGraphicObject geo = new GeoGraphicObject();
		geo.fromXML(geoMsg);
		
		//��Ӽ�¼����ǰ���ռ�¼��
		boolean bAdd = m_GeoMesRecordset.addNew(geo);
		m_GeoMesRecordset.update();
		
		if (bAdd) {
			m_MapControl.getMap().refresh();
			
			return true;
		} else {
			MyApplication.getInstance().showError("��Ӽ�¼ʧ��");
			return false;
		}
	}
	
	/**
	 * ��ȡ�������紫���XML��,ֻ֧��һ������
	 * @Parma geometry
	 */
	private String getSendGeoXML(Geometry geometry){
		if(geometry == null){
			return null;
		}
		
		String geoXML = geometry.toXML();
		if(geoXML == null){
			return null;
		}
		
		//���һ��ͨ��StringBuilder������ַ���
		String geoBuilderXML = new StringBuilder(geoXML).toString();
		
		return geoBuilderXML;
	}

	/**
	 * ��ȡ��ǰ���ڱ༭�ļ��ζ���,������ʱ������Ϣ
	 */
	private Geometry getCurrentGeoMetry(){
		Geometry geo = m_MapControl.getCurrentGeometry();
		
		if(geo != null){
			return geo;
		}
		
		return null;
	}
	
	/**
	 * ��ȡ���紫���xml��,֧�ֶ������ͬʱ����,����ͽ�������
	 */
	private String getSendXML(){
		return null;
	}
	
	/**
	 * ��Ӵ������ȡ��xml��,֧�ֶ������ͬʱ����,����ͽ�������
	 */
	private boolean addRecivedXML(String geoXMLs){
		return false;
	} 
	
	/**
	 * ��ʾ
	 */
   public void show(View parent){
		DisplayMetrics dm = m_MapControl.getContext().getResources().getDisplayMetrics();
//		showAt(0, 0, 400 * (int)dm.density, 90 * (int)dm.density);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		
		Rect outRect = new Rect();
		m_MainActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
		
		showAtLocation(m_MainView, Gravity.LEFT | Gravity.TOP, 8, (int)((120 * dm.density)/2)+ 10 + outRect.top);
   }
	
	private void showAt(int x,int y, int width, int height)
	{
		this.setFocusable(false);
		DisplayMetrics dm = m_MapControl.getContext().getResources().getDisplayMetrics();
		setWidth(width);
		setHeight(height);
		showAtLocation(m_MapControl.getRootView(), Gravity.CENTER|Gravity.CENTER/*Gravity.LEFT|Gravity.TOP*/, (int)dm.density*x, (int)dm.density*y);
	}
	
	public void dismiss(){
		super.dismiss();
		this.setFocusable(false);
	}
}