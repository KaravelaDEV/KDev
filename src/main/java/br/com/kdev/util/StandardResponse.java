package br.com.kdev.util;

public class StandardResponse {
    private StatusResponse status;
    private String message;
    private String data;


    public StandardResponse(StatusResponse status, String message, String data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getResponse(){
        return String.format("{'status' : %s, 'message' : %s, 'data' : %s}", status, message, data);
    }
}
