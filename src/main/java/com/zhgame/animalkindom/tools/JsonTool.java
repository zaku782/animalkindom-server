package com.zhgame.animalkindom.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTool {

    static ObjectMapper mapper;

    public static String toString(Object object) {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
