package com.gofynd.vision.stark.config;

import com.gofynd.vision.stark.pojo.ProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by muralidhar on 12/3/18.
 */
@Component
public class AppContext {
    @Autowired private ApplicationContext applicationContext;

    private Map<String,ProfileModel> profileModelMap = new HashMap<>();

    public String basePath(){
        return "http://localhost:"
            +applicationContext.getEnvironment().getProperty("server.port")
            +applicationContext.getApplicationName()
            +"/api/v1/execute";
    }

    public void addService(ProfileModel profileModel){
        this.profileModelMap.put(profileModel.getName().toLowerCase(),profileModel);
    }

    public ProfileModel getService(String profileName){
        return this.profileModelMap.get(profileName.toLowerCase());
    }
}
