package com.example.hp.stepcount.Http;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bin on 10/02/2017.
 */

public abstract class Task<T> implements Runnable {

    protected int flag = 0;

    protected MagicClient magicServer;

    protected OnTaskFinishedListener<Object> taskFinishedListener = null;

    protected Timestamp createTime = new Timestamp(System.currentTimeMillis());
    //执行失败次数
    protected int errorTime = 0;

    protected String successMessage;

    protected String errorMessage;

    protected int retry = 3;//如果遇到网络等问题,重启至多运行次数字.

    protected Task after;//执行完成当前任务后，需要执行的任务。

    protected Map<String, Object> tags = null;

    public void setOnTaskFinishedListener(OnTaskFinishedListener<T> taskFinishedListener){
        this.taskFinishedListener = (OnTaskFinishedListener<Object>) taskFinishedListener;
    }

    public OnTaskFinishedListener<T> getOnTaskFinishedListener() {
        return (OnTaskFinishedListener<T>) taskFinishedListener;
    }

    public String getName(){
        return null;
    }

    public Timestamp getCreateTime(){
        return createTime;
    }

    public int getType(){
        return 0;
    }

    public int getErrorTime() {
        return errorTime;
    }

    public void addErrorTime(){
        errorTime++;
    }

    public MagicClient getMagicServer() {
        return magicServer;
    }

    public void setMagicServer(MagicClient magicServer) {
        this.magicServer = magicServer;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    public Task getAfter() {
        return after;
    }

    public void setAfter(final Task after) {
        this.after = after;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTag(String tag, Object obj){
        if(tags == null){
            tags = new HashMap<String, Object>();
        }
        tags.put(tag, obj);
    }

    public Object getTag(String tag){
        if(tags == null){
            return null;
        }
        return tags.get(tag);
    }

    /**
     * 如果遇到网络等问题,重启至多运行次数字.
     * @return
     */
    public int getRetry() {
        return retry;
    }

    /**
     * 如果遇到网络等问题,重启至多运行次数字.
     * @return
     */
    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();

        sb.append("flag").append(":").append(flag).append(", ");
        if(createTime != null){
            sb.append("createTime").append(":").append(DateUtils.formatDate(new Date(createTime.getTime()), DateUtils.SDF_yyyy_MM_dd_HH_mm_ss_SSS)).append(", ");
        }

        sb.append("errorTime").append(":").append(errorTime).append(", ");
        sb.append("successMessage").append(":").append(successMessage).append(", ");
        sb.append("errorMessage").append(":").append(errorMessage).append(", ");
        sb.append("retry").append(":").append(retry).append(", ");
        if(after != null){
            Task t = after;
            boolean circle = false;
            do{
                if(t == this){
                    circle = true;
                    break;
                }
                t = t.getAfter();
            }while(t != null);
            if(!circle){
                sb.append("after").append(":{").append(after.toString()).append("}, ");
            }
        }
        return sb.toString();
    }
}
