package com.micro.hellowold;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.micro.hellowold.ui.StretchableFloatingButton;
import com.micro.hellowold.wxapi.WXPayEntryActivity;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import libcore.io.DiskLruCache;


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
    private static final int REQUECT_CODE_SDCARD = 0;

    public static Tencent mTencent;
    @BindView(R.id.bt_zxing)
    Button btZxing;
    @BindView(R.id.bt_permissions)
    Button btPermissions;
    @BindView(R.id.bt_UI)
    Button btUI;
    @BindView(R.id.sf_btn)
    StretchableFloatingButton sfBtn;
    @BindView(R.id.bt_recycle)
    Button btRecycle;
    @BindView(R.id.bt_DiskLruCache)
    Button btDiskLruCache;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.bt_DiskLruCache1)
    Button btDiskLruCache1;
    private IUiListener listener;




    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断

    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode

    DiskLruCache mDiskLruCache = null;

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
                MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.CALL_PHONE);
                //requestPermission(Manifest.permission.CAMERA, CAMERA);
            }
        });

        btUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UIActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        sfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfBtn.onStartOnclick();
            }
        });

        btRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecycleActivity.class);
                startActivity(intent);
            }
        });

        btDiskLruCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    File cacheDir = getDiskCacheDir(getApplicationContext(), "bitmap");
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }
                    mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(getApplicationContext()), 1, 10 * 1024 * 1024);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
                            String key = hashKeyForDisk(imageUrl);
                            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                            if (editor != null) {
                                OutputStream outputStream = editor.newOutputStream(0);
                                if (downloadUrlToStream(imageUrl, outputStream)) {
                                    editor.commit();
                                } else {
                                    editor.abort();
                                }
                            }
                            mDiskLruCache.flush();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });

        btDiskLruCache1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
                    String key = hashKeyForDisk(imageUrl);
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        iv.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    //shouldShowRequestPermissionRationale主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
    private void requestPermission(String permission, int requestCode) {
        if (!isGranted(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            //直接执行相应操作了
            Toast.makeText(getApplication(), "11", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String jpgPath = getCacheDir() + "test.jpg";
                takePhotoByPath(jpgPath, 2);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    /**
     * 拍照,返回拍照文件的绝对路径
     */
    private String takePhotoByPath(String filePath, int requestCode) {
        File file = new File(filePath);
        startActivityForResult(getTakePhotoIntent(file), requestCode);
        return file.getPath();
    }


    public boolean isGranted(String permission) {
        return !isMarshmallow() || isGranted_(permission);
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean isGranted_(String permission) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }

    private Intent getTakePhotoIntent(File file) {
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "10086"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }


    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
