package br.com.kdev.util;

import spark.Request;

public class ValidateParams {
    private Request request;

    public ValidateParams(Request request){
        this.request = request;
    }

    public  boolean hasParams(){
        return (request.queryParams().size() > 0);
    }

    public boolean hasParamQuery(){
        return (this.request.queryParams("query") != null);
    }

    public String getQuery(){
        return this.request.queryParams("query");
    }

    public boolean hasParamStatus(){
        return (this.request.queryParams("status") != null);
    }

    public boolean isStatusValid(){
        String status = this.request.queryParams("status");
        return (status != null && isInteger(status));
    }

    public int getParamStatus(){
        return Integer.parseInt(this.request.queryParams("status"));
    }

    private static boolean isInteger(String str){
        try {
            int d = Integer.parseInt(str);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
}
