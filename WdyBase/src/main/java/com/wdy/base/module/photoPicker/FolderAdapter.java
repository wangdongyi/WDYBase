package com.wdy.base.module.photoPicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdy.base.module.R;
import com.wdy.base.module.listen.OnRecyclerClickListen;
import com.wdy.base.module.photoPicker.model.PhotoDirectory;
import com.wdy.base.module.photoPicker.utils.PhotoUtils;

import java.util.ArrayList;

/**
 * 作者：王东一
 * 创建时间：2017/4/25.
 * 图片目录适配器
 */

public class FolderAdapter extends RecyclerView.Adapter {
    private ArrayList<PhotoDirectory> mData;
    private Context mContext;
    private int mWidth;
    private OnRecyclerClickListen onRecyclerClickListen;

    public OnRecyclerClickListen getOnRecyclerClickListen() {
        return onRecyclerClickListen;
    }

    public void setOnRecyclerClickListen(OnRecyclerClickListen onRecyclerClickListen) {
        this.onRecyclerClickListen = onRecyclerClickListen;
    }

    public FolderAdapter(Context mContext, ArrayList<PhotoDirectory> list) {
        this.mData = list;
        this.mContext = mContext;
        mWidth = PhotoUtils.dip2px(mContext, 90);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.picker_item_floder_layout, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.selectIV.setVisibility(View.GONE);
        viewHolder.photoIV.setImageResource(R.mipmap.default_picture);
        PhotoDirectory folder = mData.get(position);
        if (folder.isSelected()) {
            viewHolder.selectIV.setVisibility(View.VISIBLE);
        }
        viewHolder.folderNameTV.setText(folder.getName());
        viewHolder.photoNumTV.setText(folder.getPhotos().size() + "张");
        if (folder.getPhotos().size() > 0) {
            Glide.with(mContext).load(folder.getPhotos().get(0).getPath()).thumbnail(0.1f).into(viewHolder.photoIV);
            viewHolder.picker_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnRecyclerClickListen() != null) {
                        getOnRecyclerClickListen().onClick(position);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoIV;
        private TextView folderNameTV;
        private TextView photoNumTV;
        private ImageView selectIV;
        private LinearLayout picker_item_layout;

        ViewHolder(View view) {
            super(view);
            photoIV = (ImageView) view.findViewById(R.id.imageView_folder_img);
            folderNameTV = (TextView) view.findViewById(R.id.textView_folder_name);
            photoNumTV = (TextView) view.findViewById(R.id.textView_photo_num);
            selectIV = (ImageView) view.findViewById(R.id.imageView_folder_select);
            picker_item_layout = (LinearLayout) view.findViewById(R.id.picker_item_layout);
        }
    }
}
