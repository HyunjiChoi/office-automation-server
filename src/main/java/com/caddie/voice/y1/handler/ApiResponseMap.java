package com.caddie.voice.y1.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiResponseMap  extends ApiResponse{
    private Map responseData = new HashMap();

    public ApiResponseMap(){
        setSuccess(responseData);
    }

    public void setResponseData(String key, Object value){
        this.responseData.put(key,value);
    }

    public void setResponseTotalCount(Object value){
        this.responseData.put("totalCount",value);
    }

    /*public int getCount(){
        if(this.responseData.get("count") != null){
            return Integer.parseInt(""+this.responseData.get("count"));
        }
        return 0;
    }*/

    public void setResponseListData(String key, List<?> value){
        this.responseData.put(key,value);
        this.responseData.put("count", value.size());

    }

    public void setResponseUid(int uid){
        this.responseData.put("uid",uid);

    }
}
