package com.example.hp.stepcount.Message;

/**
 * Created by HP on 2017/6/26.
 */

public class Sleep {
    private String SLEEP_ID;
    private String SLEEP_DATE;
    private int SLEEP_DURATION;
    private int SLEEP_DEEP_DURATION;
    private int SLEEP_LOW_DURATION;
    private String SLEEP_START_TIME;
    private String SLEEP_END_TIME;
    private int SLEEP_GETUP_TIMES;
    private String SLEEP_ESTIMATE;

    public void setSid(String SLEEP_ID){
        this.SLEEP_ID = SLEEP_ID;
    }

    public String getSid(){
        return SLEEP_ID;
    }

    public void setSdate(String SLEEP_DATE){
        this.SLEEP_DATE = SLEEP_DATE;
    }

    public String getSdate(){
        return SLEEP_DATE;
    }

    public void setSduration(int SLEEP_DURATION){
        this.SLEEP_DURATION = SLEEP_DURATION;
    }

    public int getSduration(){
        return SLEEP_DURATION;
    }

    public void setSdeepduration(int SLEEP_DEEP_DURATION){
        this.SLEEP_DEEP_DURATION = SLEEP_DEEP_DURATION;
    }

    public int getSdeepduration(){
        return SLEEP_DEEP_DURATION;
    }

    public void setSlowduration(int SLEEP_LOW_DURATION){
        this.SLEEP_LOW_DURATION = SLEEP_LOW_DURATION;
    }

    public int getSlowduration(){
        return SLEEP_LOW_DURATION;
    }

    public void setSstarttime(String SLEEP_START_TIME){
        this.SLEEP_START_TIME = SLEEP_START_TIME;
    }

    public String getSstarttime(){
        return SLEEP_START_TIME;
    }

    public void setSendtime(String SLEEP_END_TIME){
        this.SLEEP_END_TIME = SLEEP_END_TIME;
    }

    public String getSendtime(){
        return SLEEP_END_TIME;
    }

    public void setSgetuptimes(int SLEEP_GETUP_TIMES){
        this.SLEEP_GETUP_TIMES = SLEEP_GETUP_TIMES;
    }

    public int getSgetuptimes(){
        return SLEEP_GETUP_TIMES;
    }

    public void setSestimate(String SLEEP_ESTIMATE){
        this.SLEEP_ESTIMATE = SLEEP_ESTIMATE;
    }

    public String getSestimate(){
        return SLEEP_ESTIMATE;
    }


}
