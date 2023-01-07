package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {
    private static Pattern extractPrivateKey = Pattern.compile("setPrivateKey\\(\"(.+)\"\\)");
    private static Pattern extractMessage = Pattern.compile("decrypt\\(\"(.+)\"\\)");

    private static Pattern extractSalt = Pattern.compile("salt=\"(.+)\"");

    private static ObjectMapper objectMapper = new ObjectMapper();
    public static String[] extractDecryptionData(String responseBody) {
        Matcher privateKeyMatcher = extractPrivateKey.matcher(responseBody);
        Matcher messageMatcher = extractMessage.matcher(responseBody);
        Matcher saltMatcher = extractSalt.matcher(responseBody);
        String privateKey = "", message = "", salt = "";
        if (privateKeyMatcher.find()) {
            privateKey = privateKeyMatcher.group(1);
        }
        if (messageMatcher.find()) {
            message = messageMatcher.group(1);
        }
        if (saltMatcher.find()) {
            salt = saltMatcher.group(1);
        }
        return new String[]{privateKey, message, salt};
    }

    public static String parseMapToJson(Map<String, String> body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getValueByKeyFromJson(String json, String key) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            return jsonNode.get(key).asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
