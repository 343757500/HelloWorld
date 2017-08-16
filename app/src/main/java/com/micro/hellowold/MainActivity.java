package com.micro.hellowold;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.micro.hellowold.interfaces.ILoginCallBack;
import com.micro.hellowold.tools.LoginHttpModule;
import com.micro.hellowold.tools.LoginManager;
import com.micro.hellowold.wxapi.WXPayEntryActivity;
import com.tencent.connect.UserInfo;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_login)
    Button btLogin;
    UserInfo mInfo;
    @BindView(R.id.tv_returnmessage)
    TextView tvReturnmessage;
    @BindView(R.id.ig_UserAvatar)
    ImageView igUserAvatar;
    @BindView(R.id.bt_pay)
    Button btPay;

    public static final String APP_ID = "wx690b9cf56a4ec056";

    public static Tencent mTencent;
    private IUiListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTencent = Tencent.createInstance("1106350900", getApplicationContext());

        ButterKnife.bind(this);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginQQ();
                LoginManager loginManager=new LoginManager();
                loginManager.loginThird(MainActivity.this, LoginHttpModule.LoginThirdType.QQ, mTencent, new ILoginCallBack() {
                    @Override
                    public void onLoginSuccess(String msg,IUiListener iUiListener) {
                        Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
                        listener=iUiListener;
                        tvReturnmessage.setText(msg);
                    }

                    @Override
                    public void onLoginFailed(String msg,IUiListener iUiListener) {
                        Toast.makeText(getApplicationContext(), "222", Toast.LENGTH_SHORT).show();
                        listener=iUiListener;
                        tvReturnmessage.setText(msg);

                    }
                });

            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WXPayEntryActivity.class);
                startActivity(intent);
            }
        });

    }


    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }

}
