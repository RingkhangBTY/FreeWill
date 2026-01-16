package com.ringkhang.freewill.helperClasses;

public class AnyResponse <T>{
    private String massage;
    private T obj;

    public AnyResponse(String massage, T obj) {
        this.massage = massage;
        this.obj = obj;
    }

    public String getMassage() {
        return massage;
    }

    public T getObj() {
        return obj;
    }
}
