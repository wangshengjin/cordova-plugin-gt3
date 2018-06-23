package com.gt3;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;

import android.text.TextUtils;
import android.util.Log;

public class NBGeetest extends CordovaPlugin {
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String g_challenge;

    @Override
    protected void pluginInitialize() {

        super.pluginInitialize();

    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        gt3GeetestUtils = new GT3GeetestUtilsBind(cordova.getActivity());
    }

    private void getGeetest(String captchaURL, CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                gt3GeetestUtils.getGeetest(cordova.getActivity(), captchaURL, "", null, new GT3GeetestBindListener() {
                    /**
                     * num 1 点击验证码的关闭按钮来关闭验证码
                     * num 2 点击屏幕关闭验证码
                     * num 3 点击返回键关闭验证码
                     */
                    @Override
                    public void gt3CloseDialog(int num) {
                        Map<String, String> validateParams = new HashMap<>();
                        validateParams.put("gt3Close", String.valueOf(num));
                        JSONObject closeJson = new JSONObject();
                        try {
                            closeJson.put("errType", validateParams);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, closeJson.toString()));
                    }


                    /**
                     * 验证码加载准备完成
                     * 此时弹出验证码
                     */
                    @Override
                    public void gt3DialogReady() {
                    }


                    /**
                     * 拿到第一个url（API1）返回的数据
                     * 该方法只适用于不使用自定义api1时使用
                     */
                    @Override
                    public void gt3FirstResult(JSONObject jsonObject) {
                        try {
                            g_challenge = (String) jsonObject.get("challenge");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("aa", "bb");
                    }


                    /**
                     * 往API1请求中添加参数
                     * 该方法只适用于不使用自定义api1时使用
                     * 添加数据为Map集合
                     * 添加的数据以get形式提交
                     */
                    @Override
                    public Map<String, String> gt3CaptchaApi1() {
                        Map<String, String> map = new HashMap<String, String>();
                        return map;
                    }

                    /**
                     * 设置是否自定义第二次验证ture为是 默认为false(不自定义)
                     * 如果为false后续会走gt3GetDialogResult(String result)拿到api2需要的参数
                     * 如果为true后续会走gt3GetDialogResult(boolean a, String result)拿到api2需要的参数
                     * result为二次验证所需要的数据
                     */
                    @Override
                    public boolean gt3SetIsCustom() {
                        return true;
                    }

                    /**
                     * 拿到第二个url（API2）需要的数据
                     * 该方法只适用于不使用自定义api2时使用
                     */
                    @Override
                    public void gt3GetDialogResult(String result) {
                    }


                    /**
                     * 自定义二次验证，也就是当gtSetIsCustom为ture时才执行
                     * 拿到第二个url（API2）需要的数据
                     * 在该回调里面自行请求api2
                     * 对api2的结果进行处理
                     */
                    @Override
                    public void gt3GetDialogResult(boolean status, String result) {

                        if (status) {
                            try {
                                JSONObject res_json = new JSONObject(result);
                                res_json.put("g_challenge", g_challenge);
                                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, res_json.toString()));
                                gt3GeetestUtils.gt3TestClose();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /**
                             * 基本使用方法：
                             *
                             * 1.取出该接口返回的三个参数用于自定义二次验证
                             * JSONObject res_json = new JSONObject(result);
                             *
                             * Map<String, String> validateParams = new HashMap<>();
                             *
                             * validateParams.put("geetest_challenge", res_json.getString("geetest_challenge"));
                             *
                             * validateParams.put("geetest_validate", res_json.getString("geetest_validate"));
                             *
                             * validateParams.put("geetest_seccode", res_json.getString("geetest_seccode"));
                             *
                             * 新加参数可以继续比如
                             *
                             * validateParams.put("user_key1", "value1");
                             *
                             * validateParams.put("user_key2", "value2");
                             *
                             * 2.自行做网络请求，请求时用上前面取出来的参数
                             *
                             * 3.拿到网络请求后的结果，判断是否成功
                             *
                             * 二次验证成功调用 gt3GeetestUtils.gt3TestFinish();
                             * 二次验证失败调用 gt3GeetestUtils.gt3TestClose();
                             */
                        }
                    }


                    /**
                     * 需要做验证统计的可以打印此处的JSON数据
                     * JSON数据包含了极验每一步的运行状态和结果
                     */
                    @Override
                    public void gt3GeetestStatisticsJson(JSONObject jsonObject) {
                    }

                    /**
                     * 往二次验证里面put数据
                     * put类型是map类型
                     * 注意map的键名不能是以下三个：geetest_challenge，geetest_validate，geetest_seccode
                     * 该方法只适用于不使用自定义api2时使用
                     */
                    @Override
                    public Map<String, String> gt3SecondResult() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("testkey", "12315");
                        return map;

                    }

                    /**
                     * 二次验证完成的回调
                     * 该方法只适用于不使用自定义api2时使用
                     * result为俄二次验证后的数据
                     * 根据二次验证返回的数据判断此次验证是否成功
                     * 二次验证成功调用 gt3GeetestUtils.gt3TestFinish();
                     * 二次验证失败调用 gt3GeetestUtils.gt3TestClose();
                     */
                    @Override
                    public void gt3DialogSuccessResult(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jobj = new JSONObject(result);
                                String sta = jobj.getString("status");
                                if ("success".equals(sta)) {
                                    gt3GeetestUtils.gt3TestFinish();
                                } else {
                                    gt3GeetestUtils.gt3TestClose();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            gt3GeetestUtils.gt3TestClose();
                        }
                    }

                    /**
                     * 验证过程错误
                     * 返回的错误码为判断错误类型的依据
                     */

                    @Override
                    public void gt3DialogOnError(String error) {
                        Log.i("dsd", "gt3DialogOnError");

                    }
                });
                //设置是否可以点击屏幕边缘关闭验证码
                gt3GeetestUtils.setDialogTouch(true);
            }
        });
    }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("NBGeetest", "execute action:" + action);
        if (action.equals("getGeetest")) {
            String captchaURL = args.getString(0);
            getGeetest(captchaURL, callbackContext);
            return true;
        }
        return false;
    }


}