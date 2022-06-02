package com.kakaopay.contactmanage.help.globalDto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorResponseDto<T> {
    private RequestDto<T> requestDto;
    private String error;
    private ObjectMapper objectMapper;

    public ErrorResponseDto(String requestUrl, T requestDto, String errMsg){
        this.requestDto = new RequestDto<>(requestUrl, requestDto);
        this.error = errMsg;
        this.objectMapper = new ObjectMapper();
    }

    public class RequestDto<T>{
        private String url;
        private T requestData;

        public RequestDto(String url, T requestData) {
            this.url = url;
            this.requestData = requestData;
        }
    }

    public String toJsonString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(convert2Map());
    }

    public Map convert2Map() {
        LinkedHashMap<String,Object> map = new LinkedHashMap();
        String defaultErrorMsg = "오류가 발생했습니다.";
        if(requestDto!=null){
            if(requestDto.url!=null) map.put("url",requestDto.url);
            if(requestDto.requestData!=null) map.put("requestData",requestDto.requestData);
        }
        map.put("error", error!=null && !error.isEmpty()? error : defaultErrorMsg);

        return map;
    }
}
