package com.micro.hellowold.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.micro.hellowold.MainActivity;
import com.micro.hellowold.interfaces.ILoginCallBack;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;


/**
 * Created by zsp on 2017/8/16.
 */

public class LoginManager {
    private static Activity getApplicationContext;
    private boolean isServerSideLogin = false;
    private static Tencent mTencent;
    private ILoginCallBack callBrack;
    UserInfo mInfo;
    /**
     * 第三方登录
     *
     * <p>
     * 调用该方法的Activit
     * @param context
     * @param
     */

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /** 获取用户信息成功 */
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    Log.e("", "获取用户信息成功，返回结果：" + response.toString());
                    //tvReturnmessage.setText("登录成功\n" + "\n昵称:" + response.getString("nickname") + "\n头像地址:" + response.get("figureurl_qq_1"));
                    //x.image().bind(igUserAvatar, response.get("figureurl_qq_1").toString());
                    if (callBrack!=null){
                        try {
                            callBrack.onLoginSuccess("登录成功\n" + "\n昵称:" + response.getString("nickname") + "\n头像地址:" + response.get("figureurl_qq_1"),loginListener);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("11", "22");
                }
            } else if (msg.what == 1) {
                //tvReturnmessage.setText(msg + "");
            } else if (msg.what == 2) {
                //tvReturnmessage.setText(msg + "");

            }
        }

    };

    public void loginQQ() {
        /** 判断是否登陆过 */
        if (!mTencent.isSessionValid()) {
            mTencent.login(getApplicationContext,"all", loginListener);
        }/** 登陆过注销之后在登录 */
        else {
            mTencent.logout(getApplicationContext);
            mTencent.login(getApplicationContext, "all", loginListener);
        }
    }




    public void loginThird(Activity context, LoginHttpModule.LoginThirdType loginThirdType, Tencent mTencent1, ILoginCallBack iLoginCallBack){
        getApplicationContext=context;
        mTencent=mTencent1;
        callBrack=iLoginCallBack;
        if (loginThirdType==LoginHttpModule.LoginThirdType.QQ){
            loginQQ();


        }else{

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
     * QQ登录第一步：获取token和openid
     */
    public class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Toast.makeText(getApplicationContext, "登录失败", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Toast.makeText(getApplicationContext, "登录失败", Toast.LENGTH_SHORT).show();
                return;
            }
            //Log.e("", "QQ登录成功返回结果-" + response.toString());
            Toast.makeText(getApplicationContext, "QQ登录成功返回结果", Toast.LENGTH_SHORT).show();
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject response) {

        }

        @Override
        public void onError(UiError e) {
            //Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
            // Util.dismissDialog();
            Toast.makeText(getApplicationContext, "登录报错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext, "授权取消！", Toast.LENGTH_LONG).show();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    //**QQ登录第二步：存储token和openid

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
          String  token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
           String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
           String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
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
            mInfo = new UserInfo(getApplicationContext, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
            Toast.makeText(getApplicationContext, "111", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * -------------------------QQ第三方登录结束--------------------
     */




}
