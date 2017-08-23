package com.example.hp.stepcount.Http; /**
 * Created by bin on 10/02/2017.
 */
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * http任务
 * @date 2014.11.10
 * @author wuliuhua
 *
 */
public class HttpTask extends Task {

    private static final String tag = "HttpTask";

    public static final String METHOD_GET = "Get";
    public static final String METHOD_POST = "Post";

    public static final int STATUS_OK = 200;

    // http 发送方式 get或post
    protected String method = null;
    // 地址
    protected String url = null;
    // 参数
    protected Map<String, Object> params = null;

    //true:保留现在数据,增量插入,false:删除本地数据,重新插入数据.
    protected boolean refresh = false;

    protected int readTimeout = 5000;//单位毫秒
    protected int connectTimeout = 2000;//单位毫秒



    public HttpTask(String url) {
        this.method = HttpTask.METHOD_GET;
        this.url = url;
    }

    public HttpTask(String url, OnTaskFinishedListener<Object> taskFinishedListener) {
        this.method = HttpTask.METHOD_GET;
        this.url = url;
        this.taskFinishedListener = taskFinishedListener;
    }

    public HttpTask(String url, String method) {
        if (METHOD_POST.equalsIgnoreCase(method)) {
            this.method = METHOD_POST;
        } else {
            this.method = METHOD_GET;
        }
        this.url = url;
    }

    public HttpTask(String url, String method, OnTaskFinishedListener<Object> taskFinishedListener) {
        if (METHOD_POST.equalsIgnoreCase(method)) {
            this.method = METHOD_POST;
        } else {
            this.method = METHOD_GET;
        }
        this.url = url;
        this.taskFinishedListener = taskFinishedListener;
    }

    public HttpTask(String url, String method, Map<String, Object> params) {
        if (METHOD_POST.equalsIgnoreCase(method)) {
            this.method = METHOD_POST;
        } else {
            this.method = METHOD_GET;
        }
        this.url = url;
        this.params = params;
    }

    public HttpTask(String url, String method, Map<String, Object> params, OnTaskFinishedListener<Object> taskFinishedListener) {
        if (METHOD_POST.equalsIgnoreCase(method)) {
            this.method = METHOD_POST;
        } else {
            this.method = METHOD_GET;
        }
        this.url = url;
        this.params = params;
        this.taskFinishedListener = taskFinishedListener;
    }

    public void setParam(String key, Object value){
        if(params == null){
            params = new HashMap<String, Object>();
        }
        params.put(key, value);
    }

    @Override
    public void run() {
        if (METHOD_POST.equalsIgnoreCase(method)) {
            doPost();
        } else if (METHOD_GET.equalsIgnoreCase(method)) {
            doGet();
        }
    }

    private void doGet() {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder temp = new StringBuilder();
        StringBuffer urlStr = new StringBuffer(this.url);
        StringBuffer paramStringBuffer = new StringBuffer();

        int responseCode = -1;

        if(params != null && params.size() > 0){
            JSONObject json = new JSONObject( params);
            paramStringBuffer.append( json.toJSONString() );
//            Iterator<String> iterator = params.keySet().iterator();
//            while(iterator.hasNext()){
//                String key = iterator.next();
//                if(urlStr.indexOf("?") == -1){
//                    urlStr.append("?").append(key).append("=").append(params.get(key));
//                } else {
//                    urlStr.append("&").append(key).append("=").append(params.get(key));
//                }
//            }
        }
        try{

/**********************************************************************************************************/

            url = new URL(urlStr.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(connectTimeout);
            httpURLConnection.setReadTimeout(readTimeout);
            addSessionID(httpURLConnection);

            if(paramStringBuffer.length() > 0){
                byte[] data = paramStringBuffer.toString().getBytes();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");// 请求头, 必须设置
                httpURLConnection.setRequestProperty("Content-Length", data.length + "");// 注意是字节长度, 不是字符长度
                httpURLConnection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                out.write(data);
                out.flush();
                out.close();
            }





            httpURLConnection.getOutputStream().flush();
            httpURLConnection.getOutputStream().close();
            responseCode = httpURLConnection.getResponseCode();
            InputStream in = null;
            if(responseCode == 200){
                in = httpURLConnection.getInputStream();
            } else {
                in = httpURLConnection.getErrorStream();
            }
            doCookie(httpURLConnection);
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            while ((line = rd.readLine()) != null) {
                temp.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseCode = -1;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        callback(responseCode, temp.toString());
    }


    protected void doPost() {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder temp = new StringBuilder();
        InputStream in = null;
        BufferedReader rd = null;
        int responseCode = -1;
        try{
            StringBuffer paramStringBuffer = new StringBuffer();
            if(params != null && params.size() > 0){
                JSONObject json = new JSONObject( params);
                paramStringBuffer.append( json.toJSONString() );
//                Iterator<String> iterator = params.keySet().iterator();
//                while(iterator.hasNext()){
//                    String key = iterator.next();
//                    if(paramStringBuffer.indexOf("?") == -1){
//                        paramStringBuffer.append("?").append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), "utf-8"));
//                    } else {
//                        paramStringBuffer.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), "utf-8"));
//                    }
//                }
            }
            url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(connectTimeout);
            httpURLConnection.setReadTimeout(readTimeout);
            addSessionID(httpURLConnection);
            if(paramStringBuffer.length() > 0){
//                paramStringBuffer.delete(0, 1);
                byte[] data = paramStringBuffer.toString().getBytes();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");// 请求头, 必须设置
                httpURLConnection.setRequestProperty("Content-Length", data.length + "");// 注意是字节长度, 不是字符长度
                httpURLConnection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());

                out.write(data);
                out.flush();
                out.close();
            }
            httpURLConnection.getOutputStream().flush();
            httpURLConnection.getOutputStream().close();
            doCookie(httpURLConnection);

            responseCode = httpURLConnection.getResponseCode();
            if(responseCode == 200){
                in = httpURLConnection.getInputStream();
            } else {
                in = httpURLConnection.getErrorStream();
            }

            rd = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = rd.readLine()) != null) {
                temp.append(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
            responseCode = -1;
            callback(responseCode, e.getMessage());
        } finally {
            if (rd != null){
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
        if(responseCode != -1){
            callback(responseCode, temp.toString());
        }
    }

    /**
     * 失败的连接再重新发送两次请求
     */
    private void callback(int status, Object result) {
        if(status != 200){
            addErrorTime();
            if(getErrorTime() < getRetry()){//如果遇到网络问题,重启至多运行3次.
                getMagicServer().addTask(this);
                return;
            }
        }
        if(taskFinishedListener != null){
            taskFinishedListener.onTaskFinished(status, result, this);
        }
        doAfter();
    }

    protected void doAfter(){
        if(after != null){
            getMagicServer().addTask(after);
        }
    }

    protected void doCookie(HttpURLConnection httpURLConnection){
        String key = null;
        String sessionId = null;
        if (httpURLConnection != null) {
            for (int i = 1; (key = httpURLConnection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    sessionId = httpURLConnection.getHeaderField(key);
                    sessionId = sessionId.substring(sessionId.indexOf("JSESSIONID=") + "JSESSIONID=".length(), sessionId.indexOf(";"));
                    getMagicServer().setSessionID(sessionId);
                    break;
                }
            }
        }
    }

    protected void addSessionID(HttpURLConnection httpURLConnection){
        String sessionID = getMagicServer().getSessionID();
        if(StringUtils.isNotBlank(sessionID)){
            httpURLConnection.setRequestProperty("Cookie", "JSESSIONID="+sessionID);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    /**
     * //单位毫秒
     * @param readTimeout
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * //单位毫秒
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("method").append(":").append(method).append(",");
        sb.append("url").append(":").append(url).append(",");
        sb.append("refresh").append(":").append(refresh).append(",");
        sb.append("readTimeout").append(":").append(readTimeout).append("ms,");
        sb.append("connectTimeout").append(":").append(connectTimeout).append("ms,");

        if(params != null && params.size() > 0){
            sb.append("params").append(":{");
            Iterator<String> iterator = params.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value = params.get(key);
                if(key == null || value == null){
                    continue;
                }
                if(value instanceof String){
                    sb.append(key).append(":").append(value.toString()).append(",");
                } else if(value instanceof File){
                    sb.append(key).append(":").append(((File)value).getAbsolutePath()).append(",");
                } else {
                    sb.append(key).append(":").append(value.toString()).append(",");
                }
            }
            sb.append("}, ");
        }
        sb.append(super.toString());
        return sb.toString();
    }
}
