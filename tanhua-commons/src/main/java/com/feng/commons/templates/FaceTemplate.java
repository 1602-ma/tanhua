package com.feng.commons.templates;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.feng.commons.properties.FaceProperties;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/1 23:33
 */
public class FaceTemplate {

    private FaceProperties faceProperties;

    public FaceTemplate(FaceProperties faceProperties) {
        this.faceProperties = faceProperties;
    }

    public boolean detect(byte[] bytes) {
        AipFace client = new AipFace(faceProperties.getAppId(), faceProperties.getApiKey(), faceProperties.getSecretKey());

        HashMap<String, String> options = new HashMap<>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");

        String image = Base64Util.encode(bytes);
        String imageType = "BASE64";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        Integer errorCode = (Integer)res.get("error_code");
        return errorCode == 0;
    }
}
