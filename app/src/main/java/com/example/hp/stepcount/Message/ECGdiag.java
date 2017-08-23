package com.example.hp.stepcount.Message;

/**
 * Created by HP on 2017/7/29.
 */

public class ECGdiag {
    private String ECGDIAG_ID;
    private String ECGDIAG_DATE;
    private String ECGDIAG_SIGN;
    private String ECGDIAG_RESULT;
    private String ECGDIAG_SUGGEST;
    private String ECGDIAG_ADDRESS;

    public void setEid(String ECGDIAG_ID){
        this.ECGDIAG_ID = ECGDIAG_ID;
    }

    public String getEid(){
        return ECGDIAG_ID;
    }

    public void setEdate(String ECGDIAG_DATE){
        this.ECGDIAG_DATE = ECGDIAG_DATE;
    }

    public String getEdate(){
        return ECGDIAG_DATE;
    }

    public void setEsign(String ECGDIAG_SIGN){
        this.ECGDIAG_SIGN = ECGDIAG_SIGN;
    }

    public String getEsign(){
        return ECGDIAG_SIGN;
    }

    public void setEresult(String ECGDIAG_RESULT){
        this.ECGDIAG_RESULT = ECGDIAG_RESULT;
    }

    public String getEresult(){
        return ECGDIAG_RESULT;
    }

    public void setEsuggest(String ECGDIAG_SUGGEST){
        this.ECGDIAG_SUGGEST = ECGDIAG_SUGGEST;
    }

    public String getEsuggest(){
        return ECGDIAG_SUGGEST;
    }

    public void setEaddress(String ECGDIAG_ADDRESS){
        this.ECGDIAG_ADDRESS = ECGDIAG_ADDRESS;
    }

    public String getEaddress(){
        return ECGDIAG_ADDRESS;
    }

}
