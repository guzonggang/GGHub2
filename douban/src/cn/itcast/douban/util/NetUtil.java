package cn.itcast.douban.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gdata.client.douban.DoubanService;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import cn.itcast.douban.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetUtil {

	/**
	 * �ж��Ƿ���Ҫ������֤�� 
	 */
	public static String isNeedCaptcha(Context context) throws Exception{
		String loginurl = context.getResources().getString(R.string.loginurl);
		URL url = new URL(loginurl);
		
		 URLConnection  conn  = url.openConnection();
		 Source source = new Source(conn);
		 List<Element> elements = source.getAllElements("input");
		 for(Element element : elements){
			   String result = element.getAttributeValue("name");
			   if("captcha-id".equals(result)){
				   return element.getAttributeValue("value");
			   }
		 }
		 return null;
	}
	
	public static Bitmap getImage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
		InputStream is = conn.getInputStream();
		return  BitmapFactory.decodeStream(is);
	}
	
	public static boolean Login(String name,String pwd,String captcha ,String captchaid, Context context) throws Exception{
		//1.����ȥ��������һ��api key secret. 
		String apiKey = "0e172e1c943874572a458a55bca95d05";
		String secret = "9e42c88400f289f6";

		DoubanService myService = new DoubanService("mydouban2", apiKey,
				secret);

		System.out.println("please paste the url in your webbrowser, complete the authorization then come back:");
		// 2.��ȡ����Ȩ�����ӵ�ַ 
		String url = myService.getAuthorizationUrl(null);
		System.out.println(url);
		//�ֶ��ķ�������� ���ͬ�ⰴť 
		// ͨ��httpclient ģ�� �û����ͬ����¼� 
		//��httpclient �� ��½����  �����½�ɹ���cookie http://www.douban.com/accounts/login
		// ���û����������������½�İ�ť��ʱ�� 
		// ʵ��������������������һ��post����Ϣ 
		//source=simple&
		//redir=http%3A%2F%2Fwww.douban.com&
		//form_email=itcastweibo@sina.cn&form_password=a11111&user_login=%E7%99%BB%E5%BD%95
		HttpPost httppost = new HttpPost("http://www.douban.com/accounts/login");
		//����http post�����ύ������ 
		List<NameValuePair> namevaluepairs  = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("source", "simple"));
		namevaluepairs.add(new BasicNameValuePair("redir", "http://www.douban.com"));
		namevaluepairs.add(new BasicNameValuePair("form_email", name));
		namevaluepairs.add(new BasicNameValuePair("form_password", pwd));
		namevaluepairs.add(new BasicNameValuePair("captcha-solution",captcha));
		namevaluepairs.add(new BasicNameValuePair("captcha-id",captchaid));
		
		namevaluepairs.add(new BasicNameValuePair("user_login", "��¼"));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(namevaluepairs,"utf-8");
		httppost.setEntity(entity);
		//  ����һ������� 
		DefaultHttpClient client = new DefaultHttpClient();
		// ������û���½����Ĳ��� 
		HttpResponse response = client.execute(httppost);
	    System.out.println(response.getStatusLine().getStatusCode());
	    Source source  = new Source(response.getEntity().getContent());
	    System.out.println( source.toString());
	    
	    
		// ��ȡ��½�ɹ���cookie 
	    CookieStore  cookie =  client.getCookieStore();
	    
	    
	    //����cookie���ʶ�����֤��վ 
	    // ģ���û���� ͬ�ⰴť 
	    //ck=Rw1e&oauth_token=6817c2017cc375dc38474604764a6194&
	    //oauth_callback=&ssid=9d9af9b0&confirm=%E5%90%8C%E6%84%8F
		
	    HttpPost post1 = new HttpPost(url);

	    String oauth_token =  url.substring(url.lastIndexOf("=")+1, url.length()); 
	    System.out.println(oauth_token);
	    List<NameValuePair> namevaluepairs1  = new ArrayList<NameValuePair>();
	    namevaluepairs1.add(new BasicNameValuePair("ck","Rw1e"));
	    namevaluepairs1.add(new BasicNameValuePair("oauth_token",oauth_token));
	    namevaluepairs1.add(new BasicNameValuePair("oauth_callback",""));
	    namevaluepairs1.add(new BasicNameValuePair("ssid","9d9af9b0"));
	    namevaluepairs1.add(new BasicNameValuePair("confirm","ͬ��"));
	    UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(namevaluepairs1,"utf-8");
	    post1.setEntity(entity1);
	    DefaultHttpClient client2 = new DefaultHttpClient();
	    client2.setCookieStore(cookie);
	    HttpResponse  respose1 =   client2.execute(post1);
	    InputStream is = respose1.getEntity().getContent();
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len= 0;
	    while((len = is.read(buffer))!=-1){
	    	bos.write(buffer, 0, len);
	    }
	    is.close();
	    System.out.println(new String( bos.toByteArray()));
	    
		//3. ��ȡ����Ȩ������ƺ���Կ
		ArrayList<String>  tokens = myService.getAccessToken();
		String accesstoken = tokens.get(0);
		String tokensecret = tokens.get(1);
		System.out.println(accesstoken);
		System.out.println(tokensecret);
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("accesstoken", accesstoken);
		editor.putString("tokensecret", tokensecret);
		editor.commit();
		return true;
	}
	
}
