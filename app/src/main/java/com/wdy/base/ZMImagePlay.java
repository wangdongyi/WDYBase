package com.wdy.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

/**
 * 作者：王东一
 * 创建时间：2019/1/25.
 */
public class ZMImagePlay {
    private CountDownTimer timer;//验证码定时器
    private Context context;
    private OnImageListen onImageListen;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12345:
                    onImageListen.onFinish();
                    break;
            }
        }
    };

    public ZMImagePlay(Context context, OnImageListen onImageListen) {
        this.context = context;
        this.onImageListen = onImageListen;
    }

    public void initTimer(int time) {
        //编辑定时器
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(12345);
            }
        }, time * 1000L);
    }

    public interface OnImageListen {

        void onFinish();
    }


}
