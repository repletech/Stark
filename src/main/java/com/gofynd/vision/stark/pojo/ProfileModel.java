package com.gofynd.vision.stark.pojo;

import java.util.List;

/**
 * Created by muralidhar on 12/3/18.
 */
public class ProfileModel {
    private String name;
    private String wsdlUrl;
    private List<Operation> operations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
