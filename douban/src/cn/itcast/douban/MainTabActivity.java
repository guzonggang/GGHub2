package cn.itcast.douban;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainTabActivity extends TabActivity {
	private TabHost mTabHost;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_tab);
		inflater =  LayoutInflater.from(this);
		
		mTabHost = getTabHost();
		mTabHost.addTab(getMyDoubanTab());
		mTabHost.addTab(getNewBookTab());
	   //	mTabHost.setCurrentTabByTag("newbook");
	}
 
	
	
	private TabSpec getMyDoubanTab(){
		TabSpec spec = mTabHost.newTabSpec("mydouban");
		//ָ����ǩ��ʾ������ , �����activity��Ӧ��intent����
		Intent intent = new Intent(this,MeActivity.class);
		spec.setContent(intent);
		// ���ñ�ǩ�����ֺ���ʽ
		spec.setIndicator(getIndicatorView("�ҵĶ���",R.drawable.tab_main_nav_me));
		return spec;
	}
	private TabSpec getNewBookTab(){
		TabSpec spec = mTabHost.newTabSpec("newbook");
		//ָ����ǩ��ʾ������ , �����activity��Ӧ��intent����
		Intent intent = new Intent(this,TestActivity1.class);
		spec.setContent(intent);
		// ���ñ�ǩ�����ֺ���ʽ
		spec.setIndicator(getIndicatorView("��������",R.drawable.tab_main_nav_book));
		return spec;
	}
	
	/**
	 * ��ȡ��Ŀ��ʾ��view���� 
	 */
	private View getIndicatorView(String name, int iconid){
		View view = inflater.inflate(R.layout.tab_main_nav, null);
	    ImageView ivicon =	(ImageView) view.findViewById(R.id.ivIcon);
	    TextView tvtitle =	(TextView) view.findViewById(R.id.tvTitle);
	    ivicon.setImageResource(iconid);
	    tvtitle.setText(name);
	    return view;
	}
}