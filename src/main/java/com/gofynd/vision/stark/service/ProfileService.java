package com.gofynd.vision.stark.service;

import com.gofynd.vision.stark.config.AppContext;
import com.gofynd.vision.stark.pojo.ProfileModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by muralidhar on 12/3/18.
 */
@Service
public class ProfileService {

    @Autowired private SOAPService soapService;
    @Autowired private AppContext appContext;

    private void createOperations(ProfileModel profileModel) throws Exception{
        soapService.createProfile(profileModel);
    }

    public ProfileModel saveProfile(ProfileModel profileModel) throws Exception{
        createOperations(profileModel);
        appContext.addService(profileModel);
        return profileModel;
    }

    public JSONObject execute(String serviceName, String operationName, Map<String,Object> params)
        throws Exception{
        return soapService.execute(serviceName,operationName,params);
    }

    public String getOperatioName(String serviceName, String name){
        return appContext.getService(serviceName).getOperations().stream()
            .filter( operation -> operation.getName().equalsIgnoreCase(name)).findFirst().get().getName();
    }

}
