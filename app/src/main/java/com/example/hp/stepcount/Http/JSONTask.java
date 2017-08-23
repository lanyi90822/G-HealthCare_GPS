package com.example.hp.stepcount.Http; /**
 * Created by bin on 10/02/2017.
 */



import com.alibaba.fastjson.JSON;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * http任务
 *
 * @date 2014.11.10
 * @author wuliuhua
 *
 */
public class JSONTask extends HttpTask implements
        OnTaskFinishedListener<Object>, Cloneable {

    protected OnTaskFinishedListener<com.alibaba.fastjson.JSONObject> taskFinishedListener;

    public JSONTask(String url) {
        super(url);
        super.taskFinishedListener = this;
    }

    public JSONTask(String url,
                    OnTaskFinishedListener<com.alibaba.fastjson.JSONObject> taskFinishedListener) {
        super(url);
        super.taskFinishedListener = this;
        this.taskFinishedListener = taskFinishedListener;
    }

    public JSONTask(String url, String method) {
        super(url, method);
        super.taskFinishedListener = this;
    }

    public JSONTask(String url, String method,
                    OnTaskFinishedListener<com.alibaba.fastjson.JSONObject> taskFinishedListener) {
        super(url, method);
        super.taskFinishedListener = this;
        this.taskFinishedListener = taskFinishedListener;
    }

    public JSONTask(String url, String method, Map<String, Object> params) {
        super(url, method, params);
        super.taskFinishedListener = this;
    }

    public JSONTask(String url, String method, Map<String, Object> params,
                    OnTaskFinishedListener<com.alibaba.fastjson.JSONObject> taskFinishedListener) {
        super(url, method, params);
        super.taskFinishedListener = this;
        this.taskFinishedListener = taskFinishedListener;
    }

    @Override
    public void onTaskFinished(int status, Object obj, Task task) {
        try {
            if (status == HttpTask.STATUS_OK) {
                if (obj != null && StringUtils.isNotBlank(obj.toString())) {
//                    JSONObject json = new JSONObject(obj.toString());
                    com.alibaba.fastjson.JSONObject json = JSON.parseObject(obj.toString());
                    System.out.println( json.toJSONString() );
                    if (json.containsKey("result")) {
                        int result = json.getInteger("result");
                        if (result == -1) {
                            addErrorTime();
                            if (getErrorTime() < getRetry()) {
//                                if (!(task instanceof LoginTask)) {// 如果是非登录任务，遇到session超时或未登录时，自动登录.
//                                    LoginTask loginTask = getMagicServer().getLoginTask();
//                                    if(loginTask != null){
//                                        loginTask.setAfter(this);
//                                        getMagicServer().addTask(loginTask);
//                                        return;
//                                    }
//                                }
                            }
                        }
                    }
                    if(taskFinishedListener != null){
                        taskFinishedListener.onTaskFinished(status, json, task);
                    }

                }
            } else if(taskFinishedListener != null){
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("result", "-1");
                json.put("msg", "network error");
                taskFinishedListener.onTaskFinished(status, json, task);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(taskFinishedListener != null){
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("result", "-1");
                json.put("msg", "network error");
                taskFinishedListener.onTaskFinished(status, json, task);
            }
        }
    }

    @Override
    public void setOnTaskFinishedListener(
            OnTaskFinishedListener taskFinishedListener) {
        this.taskFinishedListener = taskFinishedListener;
    }

    @Override
    public JSONTask clone(){
        JSONTask result = new JSONTask(url);
        result.method = method;
        result.url = url;
        if(params != null){
            result.params = new HashMap<String, Object>();
            Iterator<String> iterator = params.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value = params.get(key);
                result.params.put(key, value);
            }
        }
        result.refresh = refresh;
        result.readTimeout = readTimeout;
        result.connectTimeout = connectTimeout;
        result.taskFinishedListener = taskFinishedListener;
        result.flag = flag;
        result.magicServer = magicServer;
        result.createTime = new Timestamp(System.currentTimeMillis());
        result.errorTime = 0;
        result.successMessage = successMessage;
        result.errorMessage = errorMessage;
        result.retry = retry;
        result.after = after;
        if(tags != null){
            result.tags = new HashMap<String, Object>();
            Iterator<String> iterator = tags.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                result.tags.put(key, tags.get(key));
            }
        }
        return result;
    }
}
