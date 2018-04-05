package com.gofynd.vision.stark.controller;

import com.gofynd.vision.stark.pojo.ProfileModel;
import com.gofynd.vision.stark.pojo.RequestModel;
import com.gofynd.vision.stark.service.ProfileService;
import com.gofynd.vision.stark.service.SOAPService;
import com.gofynd.vision.stark.service.Transformer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {
    @Autowired private SOAPService soapService;
    @Autowired private ProfileService profileService;

    @RequestMapping(value = "/convertSoapToRest", method = RequestMethod.POST,
             consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> convertSoapToRest(HttpServletRequest request,
                                               @RequestBody RequestModel soapRequestModel) throws Exception {
        return new ResponseEntity<Object>(soapService.call(soapRequestModel.getWsdlUrl(),soapRequestModel.getWsdlOperationName(),
            soapRequestModel.getOperationKeyMap()), HttpStatus.OK);
    }

    @RequestMapping(value = "/explore", method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> save(@RequestBody ProfileModel profileModel) throws Exception{
        return new ResponseEntity<Object>(profileService.saveProfile(profileModel),HttpStatus.OK);
    }

    @RequestMapping(value = "/execute/{serviceName}/{operationName}", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> execute(@PathVariable String serviceName,
        @RequestHeader(value = "X-Transformation", defaultValue = "false") Boolean transformation,
        @PathVariable String operationName,
        @RequestParam Map<String,Object> params) throws Exception{
        JSONObject response = profileService.execute(serviceName,operationName,params);
        return new ResponseEntity<Object>(transformation ? Transformer.transforResponse(response).toString() : response.toString(),HttpStatus.OK);
    }

}

