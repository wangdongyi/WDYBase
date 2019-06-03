package com.wdy.base.module.photopicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.wdy.base.module.R;
import com.wdy.base.module.listen.OnRecyclerClickListen;
import com.wdy.base.module.photopicker.model.Photo;
import com.wdy.base.module.photopicker.utils.PhotoUtils;
import com.wdy.base.module.photopicker.widgets.SquareImageView;
import com.wdy.base.module.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王东一
 * 创建时间：2017/4/25.
 * 图片适配器
 */

public class PhotoAdapter extends RecyclerView.Adapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;

    private ArrayList<Photo> mData;
    //存放已选中的Photo数据
    private List<String> mSelectedPhotos;
    private Context mContext;
    private int mWidth;
    //是否显示相机，默认不显示
    private boolean mIsShowCamera = true;
    //照片选择模式，默认单选
    private int mSelectMode = PhotoPickerActivity.MODE_SINGLE;
    //图片选择数量
    private int mMaxNum = PhotoPickerActivity.DEFAULT_NUM;

    private View.OnClickListener mOnPhotoClick;
    private OnRecyclerClickListen onRecyclerClickListen;
    private PhotoClickCallBack mCallBack;

    public OnRecyclerClickListen getOnRecyclerClickListen() {
        return onRecyclerClickListen;
    }

    public void setOnRecyclerClickListen(OnRecyclerClickListen onRecyclerClickListen) {
        this.onRecyclerClickListen = onRecyclerClickListen;
    }

    public void setPhotoClickCallBack(PhotoClickCallBack callback) {
        mCallBack = callback;
    }

    PhotoAdapter(Context context, ArrayList<Photo> dataList) {
        this.mData = dataList;
        this.mContext = context;
        int screenWidth = PhotoUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - PhotoUtils.dip2px(mContext, 4)) / 3;
    }

    @Override
    public int getItemViewType(int position) {
        int Type = 0;
        if (position == 0 && mIsShowCamera) {
            Type = TYPE_CAMERA;
        } else {
            Type = TYPE_PHOTO;
        }
        return Type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(mWidth, mWidth);
        switch (viewType) {
            case TYPE_CAMERA:
                view = LayoutInflater.from(mContext).inflate(R.layout.picker_item_camera_layout, parent, false);
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case TYPE_PHOTO:
                view = LayoutInflater.from(mContext).inflate(R.layout.picker_item_photo_layout, parent, false);
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder(view, viewType);
                break;
        }
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        switch (getItemViewType(position)) {
            case TYPE_CAMERA:
                viewHolder.viewHolderCamera.camera_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getOnRecyclerClickListen() != null) {
                            getOnRecyclerClickListen().onClick(position);
                        }
                    }
                });
                break;
            case TYPE_PHOTO:
                viewHolder.viewHolderPhoto.photoImageView.setImageResource(R.mipmap.default_picture);
                Photo photo = mData.get(position);
                if (mSelectMode == PhotoPickerActivity.MODE_MULTI) {
                    viewHolder.viewHolderPhoto.wrapLayout.setOnClickListener(mOnPhotoClick);
                    ((SquareImageView) viewHolder.viewHolderPhoto.photoImageView).key = (photo.getPath());
                    viewHolder.viewHolderPhoto.selectView.setVisibility(View.VISIBLE);
                    if (mSelectedPhotos != null && mSelectedPhotos.contains(photo.getPath())) {
                        viewHolder.viewHolderPhoto.selectView.setSelected(true);
                        viewHolder.viewHolderPhoto.maskView.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.viewHolderPhoto.selectView.setSelected(false);
                        viewHolder.viewHolderPhoto.maskView.setVisibility(View.GONE);
                    }
                } else {
                    viewHolder.viewHolderPhoto.selectView.setVisibility(View.GONE);
                    viewHolder.viewHolderPhoto.wrapLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getOnRecyclerClickListen() != null) {
                                getOnRecyclerClickListen().onClick(position);
                            }
                        }
                    });
                }
                Glide.with(mContext).load(photo.getPath()).thumbnail(0.1f).into(viewHolder.viewHolderPhoto.photoImageView);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolderCamera viewHolderCamera;
        ViewHolderPhoto viewHolderPhoto;

        ViewHolder(View view, int type) {
            super(view);
            switch (type) {
                case TYPE_CAMERA:
                    viewHolderCamera = new ViewHolderCamera(view);
                    break;
                case TYPE_PHOTO:
                    viewHolderPhoto = new ViewHolderPhoto(view);
                    break;
            }
        }
    }

    public void setData(ArrayList<Photo> mData) {
        this.mData = mData;
    }

    public void setIsShowCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
    }

    public boolean isShowCamera() {
        return mIsShowCamera;
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }


    /**
     * 获取已选中相片
     *
     * @return 已选中相片
     */
    public List<String> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public void setSelectMode(int selectMode) {
        this.mSelectMode = selectMode;
        if (mSelectMode == PhotoPickerActivity.MODE_MULTI) {
            initMultiMode();
        }
    }


    /**
     * 初始化多选模式所需要的参数
     */
    private void initMultiMode() {
        mSelectedPhotos = new ArrayList<>();
        mOnPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = ((SquareImageView) v.findViewById(R.id.imageView_photo)).key;
                if (mSelectedPhotos.contains(path)) {
                    v.findViewById(R.id.mask).setVisibility(View.GONE);
                    v.findViewById(R.id.check_mark).setSelected(false);
                    mSelectedPhotos.remove(path);
                } else {
                    if (mSelectedPhotos.size() >= mMaxNum) {
                        ToastUtil.getToast(mContext).showMiddleToast(mContext.getString(R.string.picker_msg_maxi_capacity));
                        return;
                    }
                    mSelectedPhotos.add(path);
                    v.findViewById(R.id.mask).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.check_mark).setSelected(true);
                }
                if (mCallBack != null) {
                    mCallBack.onPhotoClick();
                }
            }
        };

    }

    private static class ViewHolderCamera {
        LinearLayout camera_item_layout;

        ViewHolderCamera(View view) {
            camera_item_layout = (LinearLayout) view.findViewById(R.id.camera_item_layout);
        }
    }

    private static class ViewHolderPhoto {
        private ImageView photoImageView;
        private ImageView selectView;
        private View maskView;
        private FrameLayout wrapLayout;

        ViewHolderPhoto(View view) {
            photoImageView = (ImageView) view.findViewById(R.id.imageView_photo);
            selectView = (ImageView) view.findViewById(R.id.check_mark);
            maskView = view.findViewById(R.id.mask);
            wrapLayout = (FrameLayout) view.findViewById(R.id.wrap_layout);
        }
    }

    /**
     * 多选时，点击相片的回调接口
     */
    public interface PhotoClickCallBack {
        void onPhotoClick();
    }
}
