package com.wdy.base.module.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.wdy.base.module.util.CodeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 作者：王东一
 * 创建时间：2018/8/28.
 */
public class SharedPreferencesUtil {
    //存储的sharedPreferences文件名
    private static final String FILE_NAME = "save_file_name";
    private static SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据到文件
     *
     * @ key
     * @ data
     */
    public void saveBean(String key, Object data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String serialize = WDYJsonUtil.toJson(data);
        editor.putString(key, serialize);
        editor.apply();
    }

    public <T> T getBean(String Key, Class<T> entityClass) {
        String s = sharedPreferences.getString(Key, null);
        T bean = WDYJsonUtil.GetEntity(WDYJsonUtil.GetJsonObjByLevel(s), entityClass);

        return bean;
    }

    public void saveList(String key, Object data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(key, serialize(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public Object getList(String key, Object Default) {
        String s = sharedPreferences.getString(key, null);
        if (CodeUtil.isEmpty(s)) {
            return Default;
        } else {
            try {
                return deSerialization(s);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return Default;
            }
        }
    }

    /**
     * 保存数据到文件
     *
     * @ key
     * @ data
     */
    public void saveData(String key, Object data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    /**
     * 从文件中读取数据
     *
     * @ key
     * @ defValue
     * @
     */
    public Object getData(String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * 反序列化对象
     */
    public Object deSerialization(String str) throws IOException, ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }

    /**
     * 序列化对象
     */
    public String serialize(Object save) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(save);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }
}
