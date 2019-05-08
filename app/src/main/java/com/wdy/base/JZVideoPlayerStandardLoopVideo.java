package com.wdy.base;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 作者：王东一
 * 创建时间：2018/12/17.
 */
public class JZVideoPlayerStandardLoopVideo extends JzvdStd {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    private onAutoCompletion onAutoCompletion;
    int UrlIndex = 0;  //剧集标识
    public JZVideoPlayerStandardLoopVideo.onAutoCompletion getOnAutoCompletion() {
        return onAutoCompletion;
    }

    public void setOnAutoCompletion(JZVideoPlayerStandardLoopVideo.onAutoCompletion onAutoCompletion) {
        this.onAutoCompletion = onAutoCompletion;
    }

    public JZVideoPlayerStandardLoopVideo(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public JZVideoPlayerStandardLoopVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        Jzvd.WIFI_TIP_DIALOG_SHOWED = true;
    }


    @Override
    public void startVideo() {
        super.startVideo();
//        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//            Log.d(TAG, "startVideo [" + this.hashCode() + "] ");
//            initTextureView();
//            addTextureView();
//            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//            JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//            JZMediaManager.setDataSource(jzDataSource);
//            JZMediaManager.instance().positionInList = positionInList;
//            onStatePreparing();
//        } else {
//            super.startVideo();
//        }
        tinyBackImageView.setVisibility(GONE);
        setAllControlsVisiblity(GONE,GONE,GONE,GONE,GONE,GONE,GONE);
        batteryTimeLayout.setVisibility(View.GONE);
        clarity.setVisibility(View.GONE);
        RelativeLayout.LayoutParams Params =  (RelativeLayout.LayoutParams)bottomProgressBar.getLayoutParams();
        Params.height=0;
        bottomProgressBar.setLayoutParams(Params);
    }

    public interface onAutoCompletion {
        void onAutoCompletion();
        void onStateAutoComplete();
    }


    @Override
    public void onAutoCompletion() {
//        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//            onStateAutoComplete();
//        } else {
//            super.onAutoCompletion();
//        }
        if (getOnAutoCompletion() != null) {
            onAutoCompletion.onAutoCompletion();
        }
    }


    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        mDialogBrightnessProgressBar.setVisibility(GONE);
    }
}
