package com.micro.hellowold.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.micro.hellowold.R;
import com.micro.hellowold.bean.TestEntity;
import com.micro.hellowold.holder.FooterHolder;
import com.micro.hellowold.holder.TestSectionBodyHolder;
import com.micro.hellowold.holder.TestSectionFooterHolder;
import com.micro.hellowold.holder.TestSectionHeaderHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/2.
 */

public class MyRecycleAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder,TestSectionHeaderHolder,TestSectionBodyHolder,TestSectionFooterHolder,FooterHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<TestEntity.BodyBean.EListBean> mDatas;

    public MyRecycleAdapter(List<TestEntity.BodyBean.EListBean> mDatas, Context mContext){
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    protected boolean hasHeader() {
        return false;
    }

    @Override
    protected int getSectionCount() {
        return 5;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 5;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected TestSectionHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new TestSectionHeaderHolder(mInflater.inflate(R.layout
                .item_section_header, parent, false));
    }

    @Override
    protected TestSectionFooterHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new TestSectionFooterHolder(mInflater.inflate(R.layout
                .item_section_footer, parent, false));
    }

    @Override
    protected TestSectionBodyHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new TestSectionBodyHolder(mInflater.inflate(R.layout.item_section_body,
                parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected FooterHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterHolder(mInflater.inflate(R.layout.layout_footer, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(TestSectionHeaderHolder holder, int section) {
        holder.tvNike.setText("nihao");
    }

    @Override
    protected void onBindItemViewHolder(TestSectionBodyHolder holder, int section, int position) {

    }

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(TestSectionFooterHolder holder, int section) {
        holder.tvLookNum.setText("100");
    }

    @Override
    protected void onBindFooterOtherViewHolder(FooterHolder holder) {

    }
}
