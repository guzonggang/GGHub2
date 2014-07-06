package cn.itcast.douban;

import cn.itcast.douban.util.NetUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginInActivity extends Activity implements OnClickListener {
	protected static final int NEED_CAPTCHA = 10;
	protected static final int NOT_NEED_CAPTCHA = 11;
	protected static final int GET_CAPTCHA_ERROR = 12;
	protected static final int LOGIN_SUCCESS=13;
	protected static final int LOGIN_FAIL=14;
	private EditText mNameEditText;
	private EditText mPwdEditText;
	private LinearLayout mCaptchaLinearLayout;
	private TextView mEditTextCaptchaValue;
	private ImageView mImageViewCaptcha;
	private Button btnLogin, btnExit;
	ProgressDialog pd ;
	String result = null;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pd.dismiss();
			switch (msg.what) {

			case NEED_CAPTCHA:
				mCaptchaLinearLayout.setVisibility(View.VISIBLE);
				Bitmap bitmap = (Bitmap) msg.obj;
				mImageViewCaptcha.setImageBitmap(bitmap);
				break;
			case NOT_NEED_CAPTCHA:
				
				break;
			case GET_CAPTCHA_ERROR:
				Toast.makeText(getApplicationContext(), "查询验证码失败", 1).show();
				break;
			case LOGIN_SUCCESS:
				finish();
				break;
			case LOGIN_FAIL:
				Toast.makeText(getApplicationContext(), "登陆失败", 1).show();
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setupView();
		setLinstener();
	}

	// 1.寻找控件
	// 2.赋值控件
	private void setupView() {
		mNameEditText = (EditText) this.findViewById(R.id.EditTextEmail);
		mPwdEditText = (EditText) this.findViewById(R.id.EditTextPassword);
		mCaptchaLinearLayout = (LinearLayout) this
				.findViewById(R.id.ll_captcha);
		mEditTextCaptchaValue = (TextView) this
				.findViewById(R.id.EditTextCaptchaValue);
		mImageViewCaptcha = (ImageView) this
				.findViewById(R.id.ImageViewCaptcha);
		btnExit = (Button) this.findViewById(R.id.btnExit);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		getCaptcha();

	}

	private void getCaptcha() {
		// 判断是否需要输入验证码
		pd= new ProgressDialog(this);
		pd.setMessage("正在查询验证码");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				
				try {
					result = NetUtil.isNeedCaptcha(getApplicationContext());
					if (result!=null) {
						//下载验证码获取他对应的图片 
						String imagepath = getResources().getString(R.string.captchaurl)+result+"&amp;size=s";
						Bitmap bitmap = NetUtil.getImage(imagepath);
						Message msg = new Message();
						msg.what=NEED_CAPTCHA;
						msg.obj = bitmap;
						handler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what=NOT_NEED_CAPTCHA;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					
					e.printStackTrace();
					Message msg = new Message();
					msg.what=GET_CAPTCHA_ERROR;
					handler.sendMessage(msg);
				}
			

			}

		}.start();
	}

	// 3.设置点击事件
	private void setLinstener() {
		btnExit.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		mImageViewCaptcha.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			final String name = mNameEditText.getText().toString();
			final String pwd = mPwdEditText.getText().toString();
			
			if("".equals(name)||"".equals(pwd)){
				Toast.makeText(this, "用户名或密码不能为空", 1).show();
				return ;
			}else{
				if(result!=null){
					final String captchavalue = mEditTextCaptchaValue.getText().toString();
					if("".equals(captchavalue)){
						Toast.makeText(this, "验证码不能为空", 1).show();
						return ;
					}else{
						login(name, pwd, captchavalue);
					}
					
				}else{
					login(name, pwd, "");
				}

			}
			break;
		case R.id.btnExit:
			finish();
			break;
		case R.id.ImageViewCaptcha:
			getCaptcha();
			break;

		}

	}

	private void login(final String name,final String pwd,final String captchavalue ){
		// 登陆的操作
		pd.setMessage("正在登陆");
		pd.show();
		new Thread(){

			@Override
			public void run() {
				  try {
				    boolean flag =	NetUtil.Login(name, pwd, captchavalue, result, getApplicationContext());
					if(flag){
						Message msg = new Message();
						msg.what=LOGIN_SUCCESS;
						handler.sendMessage(msg);
					}else {
						Message msg = new Message();
						msg.what=LOGIN_FAIL;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what=LOGIN_FAIL;
					handler.sendMessage(msg);
				}
				 
			}
		}.start();
	}
}
