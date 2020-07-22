package com.wdy.base.module.photoPicker;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.wdy.base.module.R;
import com.wdy.base.module.base.WDYBaseActivity;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.listen.OnRecyclerClickListen;
import com.wdy.base.module.permission.PMUtil;
import com.wdy.base.module.permission.PermissionUtils;
import com.wdy.base.module.photoPicker.model.Photo;
import com.wdy.base.module.photoPicker.model.PhotoDirectory;
import com.wdy.base.module.photoPicker.model.PhotoInput;
import com.wdy.base.module.photoPicker.utils.ItemDivider;
import com.wdy.base.module.photoPicker.utils.MediaStoreHelper;
import com.wdy.base.module.photoPicker.utils.PhotoUtils;
import com.wdy.base.module.util.CodeUtil;
import com.wdy.base.module.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王东一
 * 创建时间：2017/4/24.
 * 图片选择
 */

public class PhotoPickerActivity extends WDYBaseActivity implements PhotoAdapter.PhotoClickCallBack {
    public final static String TAG = "PhotoPickerActivity";

    public final static String KEY_RESULT = "picker_result";
    public final static int REQUEST_CAMERA = 1;

    /**
     * 是否显示相机
     */
    public final static String EXTRA_SHOW_CAMERA = "is_show_camera";
    /**
     * 照片选择模式
     */
    public final static String EXTRA_SELECT_MODE = "select_mode";
    /**
     * 最大选择数量
     */
    public final static String EXTRA_MAX_MUN = "max_num";
    /**
     * 单选
     */
    public final static int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public final static int MODE_MULTI = 1;
    /**
     * 默认最大选择数量
     */
    public final static int DEFAULT_NUM = 9;

    private final static String ALL_PHOTO = "所有图片";
    /**
     * 是否显示相机，默认不显示
     */
    private boolean mIsShowCamera = false;
    /**
     * 照片选择模式，默认是单选模式
     */
    private int mSelectMode = 0;
    /**
     * 最大选择数量，仅多选模式有用
     */
    private int mMaxNum;

    private RecyclerView mGridView;
    private ArrayList<PhotoDirectory> mSrcFolderMap;
    private ArrayList<Photo> mPhotoLists = new ArrayList<Photo>();
    private ArrayList<String> mSelectList = new ArrayList<String>();
    private PhotoAdapter mPhotoAdapter;
    private RecyclerView mFolderListView;
    private AppCompatImageView back_imageView;
    private TextView mPhotoNumTV;
    private TextView mPhotoNameTV;
    private TextView mPhotoSureTV;
    /**
     * 文件夹列表是否处于显示状态
     */
    boolean mIsFolderViewShow = false;
    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    boolean mIsFolderViewInit = false;

    /**
     * 拍照时存储拍照结果的临时文件
     */
    private File mTmpFile;
    private static PhotoUtils.OnPhotoBack onPhotoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_photo_picker);
//        setTitleName("选择图片");
        mSrcFolderMap = new ArrayList<>();
        initView();
        initIntentParams();
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        getPermissions(permissions, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted() {
                if (!PhotoUtils.isExternalStorageAvailable()) {
                    ToastUtil.getToast(getActivity()).showMiddleToast("没有sd卡");
                    finish();
                    return;
                }
                MediaStoreHelper.getPhotoDirs((FragmentActivity) getActivity(), new Bundle(), dirs -> {
                    mSrcFolderMap.clear();
                    mSrcFolderMap.addAll(dirs);
                    getPhotosSuccess();
                });
            }

            @Override
            public void onPermissionDenied(String[] permissions, int[] grantResults) {
                Log.e("权限返回", "失败" + permissions);
            }
        });

    }

    public static PhotoUtils.OnPhotoBack getOnPhotoBack() {
        return onPhotoBack;
    }

    public static void setOnPhotoBack(PhotoUtils.OnPhotoBack onPhotoBack) {
        PhotoPickerActivity.onPhotoBack = onPhotoBack;
    }

    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        PhotoInput photoInput = (PhotoInput) getIntent().getSerializableExtra("PhotoInput");
        //是否显示相机
        assert photoInput != null;
        mIsShowCamera = photoInput.isShowCamera();
        //是否多选
        mSelectMode = photoInput.getMode();
        //最多选择个数
        mMaxNum = photoInput.getMax();

        if (mSelectMode == MODE_MULTI) {
            //如果是多选模式，需要将确定按钮初始化以及绑定事件
            mPhotoSureTV.setVisibility(View.VISIBLE);
            mPhotoSureTV.setOnClickListener(v -> {
                List<String> list = mPhotoAdapter.getSelectedPhotos();
                if (list != null && list.size() > 0) {
                    mSelectList.addAll(mPhotoAdapter.getSelectedPhotos());
                    returnData();
                } else {
                    ToastUtil.getToast(PhotoPickerActivity.this).showMiddleToast("未选择图片");
                }
            });
        } else {
            mPhotoSureTV.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mGridView = (RecyclerView) findViewById(R.id.photo_gridView);
        mPhotoNumTV = (TextView) findViewById(R.id.photo_num);
        mPhotoNameTV = (TextView) findViewById(R.id.floder_name);
        mPhotoSureTV = (TextView) findViewById(R.id.wdy_sure);
        back_imageView = (AppCompatImageView) findViewById(R.id.back_imageView);

        findViewById(R.id.bottom_tab_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //消费触摸事件，防止触摸底部tab栏也会选中图片
                return true;
            }
        });
        back_imageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        mGridView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mGridView.addItemDecoration(new ItemDivider(this, LinearLayoutManager.HORIZONTAL, 2, ContextCompat.getColor(this, R.color.transparent)));
    }

    private void getPhotosSuccess() {
        mPhotoLists.addAll(mSrcFolderMap.get(0).getPhotos());
        mPhotoNumTV.setText(PhotoUtils.formatResourceString(getApplicationContext(), R.string.picker_photos_num, mPhotoLists.size()));
        mPhotoAdapter = new PhotoAdapter(PhotoPickerActivity.this, mPhotoLists);
        mPhotoAdapter.setIsShowCamera(mIsShowCamera);
        mPhotoAdapter.setSelectMode(mSelectMode);
        mPhotoAdapter.setMaxNum(mMaxNum);
        mGridView.setAdapter(mPhotoAdapter);
        final ArrayList<PhotoDirectory> folders = new ArrayList<PhotoDirectory>();
        for (int i = 0; i < mSrcFolderMap.size(); i++) {
            if (i == 0) {
                PhotoDirectory folder = mSrcFolderMap.get(i);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mSrcFolderMap.get(i));
            }
        }
        mPhotoNameTV.setOnClickListener(new View.OnClickListener() {
            //@TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                toggleFolderList(folders);
            }
        });
        mPhotoAdapter.setOnRecyclerClickListen(new OnRecyclerClickListen() {
            @Override
            public void onClick(int position) {
                if (mPhotoAdapter.isShowCamera() && position == 0) {
                    openCamera();
                    return;
                }
                selectPhoto(mPhotoLists.get(position));
            }

        });
    }

    /**
     * 点击选择某张照片
     *
     * @param photo 某张照片
     */
    private void selectPhoto(Photo photo) {
        if (photo == null) {
            return;
        }
        String path = photo.getPath();
        if (mSelectMode == MODE_SINGLE) {
            mSelectList.add(path);
            returnData();
        }
    }


    /**
     * 返回选择图片的路径
     */
    private void returnData() {
        // 返回已选择的图片数据
        if (getOnPhotoBack() != null) {
            getOnPhotoBack().onBack(mSelectList);
        }
        Intent data = new Intent();
        data.putStringArrayListExtra(KEY_RESULT, mSelectList);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param folders
     */
    private void toggleFolderList(final ArrayList<PhotoDirectory> folders) {
        //初始化文件夹列表
        if (!mIsFolderViewInit) {
            ViewStub folderStub = (ViewStub) findViewById(R.id.folder_stub);
            folderStub.inflate();
            View dimLayout = findViewById(R.id.dim_layout);
            mFolderListView = (RecyclerView) findViewById(R.id.listView_folder);
            final FolderAdapter adapter = new FolderAdapter(this, folders);
            mFolderListView.setLayoutManager(new LinearLayoutManager(this));
            mFolderListView.setAdapter(adapter);
            adapter.setOnRecyclerClickListen(position -> {
                for (PhotoDirectory folder : folders) {
                    folder.setIsSelected(false);
                }
                PhotoDirectory folder = folders.get(position);
                folder.setIsSelected(true);
                adapter.notifyDataSetChanged();
                mPhotoLists.clear();
                mPhotoLists.addAll(folder.getPhotos());
                if (ALL_PHOTO.equals(folder.getName())) {
                    mPhotoAdapter.setIsShowCamera(mIsShowCamera);
                } else {
                    mPhotoAdapter.setIsShowCamera(false);
                }
                //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
//                    mGridView.setAdapter(mPhotoAdapter);
                mPhotoAdapter.notifyDataSetChanged();
                mPhotoNumTV.setText(PhotoUtils.formatResourceString(getApplicationContext(), R.string.picker_photos_num, mPhotoLists.size()));
                mPhotoNameTV.setText(folder.getName());
                toggle();
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFolderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            mIsFolderViewInit = true;
        }
        toggle();
    }

    @Override
    public void onBackPressed() {
        if (mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
            return;
        }
        super.onBackPressed();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFolderViewShow = true;
        }
    }


    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        int height = PhotoUtils.getHeightInPx(this) - 2 * CodeUtil.dip2px(this, 50) ;
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height, 0);
        transOutAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }

    /**
     * 选择文件夹
     *
     * @param photoFolder
     */
    public void selectFolder(PhotoDirectory photoFolder) {
        mPhotoAdapter.setData(photoFolder.getPhotos());
        mPhotoAdapter.notifyDataSetChanged();
    }


    private void openCamera() {
        // 判断SD卡是否存在
        if (!CodeUtil.getSDStatus(this))
            return;
        Intent mIntent = new Intent();
        mTmpFile = PhotoUtils.createFile(PhotoPickerActivity.this);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", mTmpFile);//通过FileProvider创建一个content类型的Uri
            mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            imageUri = Uri.fromFile(mTmpFile);
            // 指定拍摄照片保存路径
        }
        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(mIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (mTmpFile != null) {
                    mSelectList.add(mTmpFile.getAbsolutePath());
                    returnData();
                }
                break;
        }
    }

    @Override
    public void onPhotoClick() {
        List<String> list = mPhotoAdapter.getSelectedPhotos();
    }
}
