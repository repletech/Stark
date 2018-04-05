package com.gofynd.vision.stark.pojo;

import java.util.List;

/**
 * Created by muralidhar on 12/3/18.
 */
public class Operation {
    private String name;
    private String url;
    private List<String> listOfParams;

    public List<String> getListOfParams() {
        return listOfParams;
    }

    public void setListOfParams(List<String> listOfParams) {
        this.listOfParams = listOfParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
