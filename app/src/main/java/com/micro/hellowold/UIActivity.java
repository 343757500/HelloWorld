package com.micro.hellowold;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/21.
 */

public class UIActivity extends AppCompatActivity {


    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;
    @BindView(R.id.po_image2)
    ShineButton poImage2;
    @BindView(R.id.LinearLayout)
    android.widget.LinearLayout LinearLayout;
    private PullToRefreshView mPullToRefreshView;

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
    }
}
