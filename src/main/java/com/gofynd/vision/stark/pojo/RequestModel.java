package com.gofynd.vision.stark.pojo;

import java.util.Map;

public class RequestModel {
    private String wsdlUrl;
    private String wsdlOperationName;
    private Map<String,Object> operationKeyMap;

    @Override
    public String toString() {
        return "SoapRequestModel{" +
                "wsdlUrl='" + wsdlUrl + '\'' +
                ", wsdlOperationName='" + wsdlOperationName + '\'' +
                ", operationKeyMap=" + operationKeyMap +
                '}';
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getWsdlOperationName() {
        return wsdlOperationName;
    }

    public void setWsdlOperationName(String wsdlOperationName) {
        this.wsdlOperationName = wsdlOperationName;
    }

    public Map<String, Object> getOperationKeyMap() {
        return operationKeyMap;
    }

    public void setOperationKeyMap(Map<String, Object> operationKeyMap) {
        this.operationKeyMap = operationKeyMap;
    }

}
