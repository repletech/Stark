package com.gofynd.vision.stark.service;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.settings.HttpSettings;
import com.gofynd.vision.stark.constants.AppConstant;
import com.gofynd.vision.stark.config.AppContext;
import com.gofynd.vision.stark.pojo.ProfileModel;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by muralidhar on 17/2/18.
 */
@Service public class SOAPService {
    @Autowired private AppContext appContext;
    @Autowired private ProfileService profileService;

    {
        SoapUI.getSettings().setBoolean(HttpSettings.RESPONSE_COMPRESSION, false);
    }

    private Map<String, WsdlInterface> wsdlInterfaceMap = new HashMap<>();

    public JSONObject call(String wsdlUrl, String operationName, Map<String, Object> params)
        throws Exception {

        WsdlProject project = new WsdlProject();

        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, wsdlUrl, true)[0];

        WsdlOperation operation = iface.getOperationByName(operationName);

        WsdlRequest request = operation.addNewRequest("Request");

        request.setRequestContent(operation.createRequest(true));

        populate(request, params);

        WsdlSubmit submit = (WsdlSubmit) request.submit(new WsdlSubmitContext(request), false);

        Response response = submit.getResponse();

        return XML.toJSONObject(response.getContentAsString());
    }

    public JSONObject execute(String serviceName, String operationName, Map<String, Object> params)
        throws Exception {

        WsdlInterface iface = getProject(serviceName);

        WsdlOperation operation =
            iface.getOperationByName(profileService.getOperatioName(serviceName, operationName));

        WsdlRequest request = operation.addNewRequest("Request");

        request.setRequestContent(operation.createRequest(true));

        populate(request, params);

        WsdlSubmit submit = (WsdlSubmit) request.submit(new WsdlSubmitContext(request), false);

        Response response = submit.getResponse();

        return XML.toJSONObject(response.getContentAsString());
    }

    private void populate(WsdlRequest wsdlRequest, Map<String, Object> params) {
        wsdlRequest.setRequestContent(replaceParams(wsdlRequest.getRequestContent(), params));
    }

    private String replaceParams(String requestContent, Map<String, Object> paramMap) {

        Set<String> keys = paramMap.keySet();

        for (String param : keys) {
            Pattern pattern =
                Pattern.compile(param + ">.*" + param + ">", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(requestContent);
            if (matcher.find()) {
                String value = matcher.group();
                value = value.replace("?", paramMap.get(param).toString());
                requestContent = requestContent.replaceFirst("(?i)" + pattern.toString(), value);

            }
        }

        return requestContent;
    }

    public void createProfile(ProfileModel profileModel) throws Exception {

        WsdlInterface iface =
            WsdlInterfaceFactory.importWsdl(new WsdlProject(), profileModel.getWsdlUrl(), true)[0];

        List<Operation> operationList = iface.getOperationList();
        List<com.gofynd.vision.stark.pojo.Operation> operations = new ArrayList<>();

        addProject(profileModel.getName(), iface);

        operationList.forEach(wsdlOperation -> {
            com.gofynd.vision.stark.pojo.Operation restOperation = new com.gofynd.vision.stark.pojo.Operation();

            restOperation.setName(wsdlOperation.getName());

            WsdlOperation operation = iface.getOperationByName(wsdlOperation.getName());
            WsdlRequest request = operation.addNewRequest("Request");
            request.setRequestContent(operation.createRequest(true));

            restOperation.setListOfParams(getParams(request.getRequestContent()));

            restOperation.setUrl(appContext.basePath() + "/" + profileModel.getName().toLowerCase() + "/"
                    + restOperation.getName().toLowerCase());

            operations.add(restOperation);
        });

        profileModel.setOperations(operations);
    }

    private List<String> getParams(String requestBody) {
        List<String> params = new ArrayList<>();
        Pattern pattern = Pattern.compile(AppConstant.PARAMS_REGEX);
        Matcher matcher = pattern.matcher(requestBody);
        while (matcher.find()) {
            String operationName = matcher.group().replaceAll(".*?/", "").replaceAll(">", "");
            if (operationName.contains(":"))
                operationName = operationName.replaceAll(".*?:", "");

            params.add(operationName);
        }
        return params;
    }


    private WsdlInterface getProject(String projectName) {
        return wsdlInterfaceMap.get(projectName.toLowerCase());
    }

    private void addProject(String projectName, WsdlInterface wsdlInterface) {
        wsdlInterfaceMap.put(projectName.toLowerCase(), wsdlInterface);
    }

}
