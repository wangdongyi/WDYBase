package com.wdy.base.module.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Stack;

/**
 * 作者：王东一 on 2016/3/21 16:56
 **/
public class ActivityManage {
    private Stack<Activity> activityList = new Stack<>();
    private static ActivityManage instance;


    // 单例模式中获取唯一的MyApplication实例
    public static ActivityManage getInstance() {
        if (null == instance) {
            instance = new ActivityManage();
        }
        return instance;
    }
    public Stack<Activity> getActivityList() {
        return activityList;
    }
    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 添加Activity到容器中
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void finishActivity(Class<?>... activityClasses) {
        for (Class<?> ac : activityClasses) {
            for (Activity a : activityList) {
                if(ac.toString().equals(a.getClass().toString())){
                    a.finish();
                }
            }
        }
    }

    public boolean HaveActivity(Class<?>... activityClasses) {
        boolean have = false;
        for (Class<?> ac : activityClasses) {
            for (int i = 0; i < activityList.size(); i++) {
                if(ac.toString().equals(activityList.get(i).getClass().toString())){
                    have = true;
                }
            }
        }
        return have;
    }

    public boolean getTopActivity(Context context,Class<?>... activityClasses) {
        String topPackageName = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
        topPackageName = taskInfo.topActivity.getClassName();
        return topPackageName.equals(activityClasses[0].getName());
    }

    public void removeActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    // 遍历所有Activity并finish
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

}
