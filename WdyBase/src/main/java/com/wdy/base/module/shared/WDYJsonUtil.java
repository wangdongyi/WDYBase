package com.wdy.base.module.shared;

/**
 * 作者：王东一
 * 创建时间：2017/7/12.
 */

public class WDYJsonUtil {
    private static String TAG = "JsonUtil";

//    private static Gson gson;
//
//    private static Gson getGson() {
//        if(gson==null){
//            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        }
//        return gson;
//    }
//
//    public static String toJson(Object obj) {
//        return getGson().toJson(obj);
//    }
//
//    /**
//     * @param json
//     * @param c
//     * @return
//     */
//    public static <T> T jsonToBean(String json, Class<T> c) {
//        T object = getGson().fromJson(json, c);
//        return object;
//    }
//
//    public static boolean isSuccess(String json) {
//        return "0".equals(GetStringByLevel(json, "status"));
//    }
//
//    public static JsonObject GetJsonObjByLevel(String data, String... param) {
//        JsonObject json = null;
//        try {
//            if (data != null && data != "") {
//                json = getGson().fromJson(data, JsonObject.class);
//                if (param != null && param.length > 0) {
//                    for (String item : param) {
//                        json = json.getAsJsonObject(item);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            WDYLog.e(TAG, "GetJsonObjByLevel 发生异常：" + e.getMessage());
//            json = null;
//        }
//        return json;
//    }
//
//    public static String GetStringByLevel(String data, String... param) {
//        JsonObject json = null;
//        String result = null;
//        try {
//            if (data != null && data != "") {
//                json = getGson().fromJson(data, JsonObject.class);
//                if (param != null && param.length > 0) {
//                    for (int i = 0; i < param.length - 1; i++) {
//                        json = json.getAsJsonObject(param[i]);
//                    }
//                }
//            }
//            if (json != null) {
//                result = json.getAsJsonPrimitive(param[param.length - 1]).toString();
//            }
//        } catch (Exception e) {
//            WDYLog.e(TAG, "GetStringByLevel 发生异常：" + e.getMessage());
//        }
//
//        return result == null ? null : result.replace('"', ' ').trim();
//    }
//
//    /**
//     * 从字符串中获取JsonArray，转为list时用
//     *
//     * @param data  目标字符串
//     * @param param JsonArray各级的key，不写时为整个字符串
//     * @return
//     */
//    public static JsonArray GetJsonArrayByLevel(String data, String... param) {
//        JsonArray jsonArray = null;
//        JsonObject json = null;
//        try {
//            if (data != null && data != "") {
//                if (param != null && param.length > 0) {
//                    json = getGson().fromJson(data, JsonObject.class);
//                    for (int i = 0; i < param.length - 1; i++) {
//                        json = json.getAsJsonObject(param[i]);
//                    }
//                    jsonArray = json.getAsJsonArray(param[param.length - 1]);
//                } else {
//                    jsonArray = getGson().fromJson(data, JsonArray.class);
//                }
//            }
//        } catch (Exception e) {
//            WDYLog.e(TAG, "GetJsonArrayByLevel 发生异常：" + e.getMessage());
//        }
//        return jsonArray;
//    }
//
//    public static <T> T GetEntity(JsonObject ele, Class<T> classT) {
//        T result = null;
//        try {
//            if (ele != null && ele.toString() != "") {
//                result = getGson().fromJson(ele, classT);
//            }
//        } catch (Exception e) {
//            WDYLog.e(TAG, "GetEntity 发生异常：" + e.getMessage());
//        }
//        return result;
//    }
//
//    public static <T> List<T> GetEntityS(JsonArray array, Class<T> classT) {
//        List<T> result = new ArrayList<T>();
//        T t = null;
//        try {
//            if (array != null && array.size() > 0) {
//                for (JsonElement item : array) {
//                    if (!(item == null || item.toString() == "")) {
//                        if (classT == String.class) {
//                            result.add((T) item.getAsString());
//                        } else {
//                            t = GetEntity(item.getAsJsonObject(), classT);
//                            if (t != null) {
//                                result.add(GetEntity(item.getAsJsonObject(), classT));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            WDYLog.e(TAG, "GetEntityS 发生异常：" + e.getMessage());
//        }
//        return result;
//    }
}
