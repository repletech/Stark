package com.gofynd.vision.stark.service;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by muralidhar on 17/3/18.
 */
public class Transformer {

    private static final List<String> SOAP_LITERALS = Arrays.asList("xmlns", ":");

    private static JSONObject getSOAPBody(JSONObject response) {
        try {
            List<String> keys = Arrays.asList(JSONObject.getNames(response));
            for (String key : keys) {
                if (key.equalsIgnoreCase("soap:Body")) {
                    return (JSONObject) response.get(key);
                } else {
                    if (response.get(key) instanceof JSONObject)
                        return getSOAPBody((JSONObject) response.get(key));
                }
            }
            removeSOAPLiterals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONObject transforResponse(JSONObject response) {
        return removeSOAPLiterals(getSOAPBody(response));
    }

    private static JSONObject removeSOAPLiterals(JSONObject jsonObject) {
        try {
            List<String> keys = Arrays.asList(JSONObject.getNames(jsonObject));
            for (String key : keys) {
                if (isKeyContainsAnyLiteral(key)) {
                    jsonObject.remove(key);
                } else {
                    if (jsonObject.get(key) instanceof JSONObject)
                        removeSOAPLiterals((JSONObject) jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static Boolean isKeyContainsAnyLiteral(String key) {
        return SOAP_LITERALS.parallelStream().anyMatch(literal -> key.contains(literal));
    }

}
