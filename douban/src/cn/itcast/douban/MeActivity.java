package cn.itcast.douban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private SharedPreferences sp;
	private static final String[] arr = {"�Ҷ�","�ҿ�","����","����","�ҵ��ռ�","�ҵ�����","С��"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.meactivity);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		mListView = (ListView) this.findViewById(R.id.melistview);
		mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.me_item, R.id.fav_title, arr));
		
		mListView.setOnItemClickListener(this);
	
	}

	
	
	/**
	 * �ж��û��Ƿ��ȡ������Ȩ 
	 * @return
	 */
	private boolean isUserAuthoroized(){
		String accesstoken = sp.getString("accesstoken", null);
		String tokensecret = sp.getString("tokensecret", null);
		if(accesstoken==null||tokensecret==null){
			return false;
		}else{
			return true;
		}
		
	}



	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(isUserAuthoroized()){
			//���뵽��Ӧ�Ľ��� 
		}else{
			//���򵽵�½���� 
			Intent intent = new Intent(this,LoginInActivity.class);
			startActivity(intent);
		}
		
	}
	
}
