package com.micro.hellowold;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.micro.hellowold.adapter.MyRecycleAdapter;
import com.micro.hellowold.adapter.SectionedSpanSizeLookup;
import com.micro.hellowold.bean.TestEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/2.
 */

public class RecycleActivity extends AppCompatActivity {
    @BindView(R.id.recycle)
    RecyclerView recycle;
     private  List<TestEntity.BodyBean.EListBean> testEntity=new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_activity);
        ButterKnife.bind(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        MyRecycleAdapter adapter=new MyRecycleAdapter(testEntity,getApplicationContext());
        mGridLayoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(adapter,
                mGridLayoutManager));
        recycle.setLayoutManager(mGridLayoutManager);
        recycle.setAdapter(adapter);
    }
}
