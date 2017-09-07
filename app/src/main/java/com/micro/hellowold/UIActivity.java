package com.micro.hellowold;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bt.mylibrary.TimeLineMarkerView;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/21.
 */

public class UIActivity extends Activity {


    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;
    @BindView(R.id.po_image2)
    ShineButton poImage2;
    @BindView(R.id.LinearLayout)
    android.widget.LinearLayout LinearLayout;
    @BindView(R.id.time1)
    TimeLineMarkerView time1;
    @BindView(R.id.circle_progress)
    CircleProgress circleProgress;
    private PullToRefreshView mPullToRefreshView;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==0) {
                circleProgress.setProgress((Integer) msg.obj);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.uiactivity);
        ButterKnife.bind(this);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        ShineButton shineButtonJava = new ShineButton(this);
        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        shineButtonJava.setLayoutParams(layoutParams);
        if (LinearLayout != null) {
            LinearLayout.addView(shineButtonJava);
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 1000; j++) {
                    Message message=new Message();
                    message.what=0;
                    message.obj=j;
                    handler.sendMessage(message);
                    Log.e("hei",j+"      00000000000000000000000000000");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



    }
}
