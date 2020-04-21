package com.wdy.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wdy.base.R;
import com.wdy.base.bean.RefreshTestItemBean;
import com.wdy.base.module.util.CodeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 作者：王东一
 * 创建时间：2020/4/21.
 */
public class RefreshTestAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<RefreshTestItemBean> list;

    public RefreshTestAdapter(Context mContext, List<RefreshTestItemBean> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_refresh_test_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        RefreshTestItemBean bean = list.get(position);
        if (bean != null) {
            viewHolder.text_name.setText(CodeUtil.TextEmpty(bean.getTextName()));
            RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(35));
            Glide.with(mContext)
                    .asBitmap()
                    .load(bean.getUrl())
                    .centerCrop()
                    .apply(options)
                    .into(viewHolder.image);

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView text_name;
        ImageView image;


        ViewHolder(View view) {
            super(view);
            this.rootView = view;
            this.text_name = rootView.findViewById(R.id.text_name);
            this.image = rootView.findViewById(R.id.image);
        }
    }

}
