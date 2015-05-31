package com.pfizer.cscoe.util;

import static com.pfizer.cscoe.util.ErrorUtils.*;
import static com.pfizer.cscoe.util.IoUtils.*;
import static javax.xml.bind.DatatypeConverter.*;

import java.security.*;
import java.util.*;

/**
 * This class provides methods to support authentication process used in
 * applications.
 * 
 * @author xiangx04
 * 
 */
public class LarsAuthUtils {
    /**
     * This method creates http authorization header (a customized
     * authentication header for validating service consumer). A service
     * consumer call this method to generate a value of authorization property
     * 
     * @param userName
     * @param token
     * @param uri
     * @param key
     * @return
     */
    public static String createAuthHeader(String userName, String token, String uri, String key) {
        String message = calculateSignature(uri);
        String signature = calLarsSig(message, token, key);
        StringBuilder sb = new StringBuilder();
        sb.append("lars").append(" ").append(userName).append(" ").append(token).append(" ").append(message).append(" ").append(signature);
        return sb.toString();
    }

    /**
     * This method parses http authorization header (a customized authentication
     * header for validating service consumer). After service providers received
     * the http request from service consumers. The service provider call this
     * method to parse the value of authorization header and get information
     * needed to form request to the lars authenticator.
     * 
     * @param authHeader
     * @return
     */
    public static Map<String, String> parseAuthHeader(String authHeader) {
        Map<String, String> authInfo = new HashMap<String, String>();
        List<String> elements = StringUtils.split(authHeader, " ");
        if (elements.size() != 5)
            return authInfo;
        if (!StringUtils.equal("lars", elements.get(0))) {
            return authInfo;
        }
        authInfo.put("user", elements.get(1));
        authInfo.put("client", elements.get(2));
        authInfo.put("message", elements.get(3));
        authInfo.put("signature", elements.get(4));
        return authInfo;
    }

    /**
     * Calculate signature for lars authorization
     * 
     * @param message
     * @param token
     * @param key
     * @return
     */
    public static String calLarsSig(String message, String token, String key) {
        return calculateSignature(message + token + key);
    }

    /**
     * calculate signature using SHA1 hashing algorithm, encode hash value to
     * Base64 representation.
     * 
     * @param message
     * @param apiKey
     * @return
     */
    public static String calculateSignature(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(message.getBytes(UTF8));
            return printBase64Binary(digest);
        } catch (Exception e) {
            throw wrapped(e, "exception when calculate signature");
        }
    }

}
