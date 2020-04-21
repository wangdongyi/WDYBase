package com.wdy.base.module.view.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdy.base.module.R;

import java.util.ArrayList;

/**
 * 作者：王东一
 * 创建时间：2019-12-25.
 */
public class BannerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<BannerBean> list = new ArrayList<>();


    public BannerAdapter(Context mContext, ArrayList<BannerBean> list) {
        this.mContext = mContext;
        this.list = list;

    }

    public interface onClickItem {
        void onClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_banner_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (list.get(position).isSelected()) {
            viewHolder.imageView.setImageResource(list.get(position).getPictureSelected());
        } else {
            viewHolder.imageView.setImageResource(list.get(position).getPictureUnSelected());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.adapter_banner_imageView);
        }
    }
}
