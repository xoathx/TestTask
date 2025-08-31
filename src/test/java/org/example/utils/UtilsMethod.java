package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PostDto;

import java.io.IOException;

public class UtilsMethod {
    public static String serializeJson(PostDto postDto) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(postDto);
    }
}
