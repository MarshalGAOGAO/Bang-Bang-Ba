package com.example.registerloogin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Marshal Gao on 2017/7/23.
 */

public class HomeUpAdapter extends RecyclerView.Adapter<HomeUpAdapter.ViewHolder> implements View.OnClickListener {

    private List<HomeUp> mHomeUpList;

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View homeUpView;
        TextView guideText;

        public ViewHolder(View view) {
            super(view);
            homeUpView = view;
            guideText = (TextView) view.findViewById(R.id.guide_text);
        }
    }

    public HomeUpAdapter(List<HomeUp> homeUpList) {
        mHomeUpList = homeUpList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeup_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeUp homeUp = mHomeUpList.get(position);
        holder.guideText.setText(homeUp.getGuide());
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mHomeUpList.size();
    }
}
