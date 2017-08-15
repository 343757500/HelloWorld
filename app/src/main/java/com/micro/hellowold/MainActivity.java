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

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

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

    private boolean isServerSideLogin = false;

    public static Tencent mTencent;
    private static String token;
    private static String expires;
    private static String openId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTencent = Tencent.createInstance("1106350900", getApplicationContext());

        ButterKnife.bind(this);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginQQ();

            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // "111111"
            }
        });

    }

    /**
     * ------------------------QQ第三方登录--------------------
     */
    public void loginQQ() {
        /** 判断是否登陆过 */
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
        }/** 登陆过注销之后在登录 */
        else {
            mTencent.logout(this);
            mTencent.login(this, "all", loginListener);
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    /**
     * QQ登录第二步：存储token和openid
     */
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    /**
     * QQ登录第三步：获取用户信息
     */
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    Message msg = new Message();
                    msg.obj = "把手机时间改成获取网络时间";
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onCancel() {
                    Message msg = new Message();
                    msg.obj = "获取用户信息失败";
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
            Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /** 获取用户信息成功 */
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    Log.e("", "获取用户信息成功，返回结果：" + response.toString());
                    try {
                        tvReturnmessage.setText("登录成功\n" + "\n昵称:" + response.getString("nickname") + "\n头像地址:" + response.get("figureurl_qq_1"));
                        x.image().bind(igUserAvatar, response.get("figureurl_qq_1").toString());
                        Log.e("11", "22");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 1) {
                tvReturnmessage.setText(msg + "");
            } else if (msg.what == 2) {
                tvReturnmessage.setText(msg + "");
            }
        }

    };

    /**
     * QQ登录第一步：获取token和openid
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                return;
            }
            //Log.e("", "QQ登录成功返回结果-" + response.toString());
            Toast.makeText(getApplicationContext(), "QQ登录成功返回结果", Toast.LENGTH_SHORT).show();
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject response) {

        }

        @Override
        public void onError(UiError e) {
            //Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
            // Util.dismissDialog();
            Toast.makeText(getApplicationContext(), "登录报错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    /**
     * -------------------------QQ第三方登录结束--------------------
     */


    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
    }

}
