package com.micro.hellowold;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.micro.hellowold.interfaces.ILoginCallBack;
import com.micro.hellowold.tools.LoginHttpModule;
import com.micro.hellowold.tools.LoginManager;
import com.micro.hellowold.wxapi.WXPayEntryActivity;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

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
    private static  final int REQUECT_CODE_SDCARD = 0;

    public static Tencent mTencent;
    @BindView(R.id.bt_zxing)
    Button btZxing;
    @BindView(R.id.bt_permissions)
    Button btPermissions;
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
                LoginManager loginManager = new LoginManager();
                loginManager.loginThird(MainActivity.this, LoginHttpModule.LoginThirdType.QQ, mTencent, new ILoginCallBack() {
                    @Override
                    public void onLoginSuccess(String msg, IUiListener iUiListener) {
                        Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
                        listener = iUiListener;
                        tvReturnmessage.setText(msg);
                    }

                    @Override
                    public void onLoginFailed(String msg, IUiListener iUiListener) {
                        Toast.makeText(getApplicationContext(), "222", Toast.LENGTH_SHORT).show();
                        listener = iUiListener;
                        tvReturnmessage.setText(msg);

                    }
                });

            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WXPayEntryActivity.class);
                startActivity(intent);
            }
        });


        //此处是学习属性动画的demo
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btLogin, "rotation", 0f, 360f, 0f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(btLogin, "translationX", -500f, 0f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(btLogin, "alpha", 1f, 0f, 1f);
        objectAnimator.setDuration(3000);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(objectAnimator).with(objectAnimator1).after(objectAnimator2);
        animationSet.start();


        btZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);

                integrator.initiateScan();
            }
        });


        btPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

    }




    //确保能接收到
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Tencent.onActivityResultData(requestCode, resultCode, data, listener);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            System.out.print(result);
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if(requestCode == CALL_PHONE_REQUEST_CODE){
                boolean b = grantResults == null ? true : false;
                Log.e("heima",b+"");
                if (grantResults !=null&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    callPhone();
                } else {
                    // Permission Denied
                    Toast.makeText(this,"权限被拒绝了",Toast.LENGTH_SHORT).show();
                }
            }
    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"10086"));
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }


}
