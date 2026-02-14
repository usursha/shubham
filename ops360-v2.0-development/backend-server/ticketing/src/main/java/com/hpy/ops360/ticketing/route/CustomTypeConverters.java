//package com.hpy.ops360.ticketing.route;
//
//import java.io.IOException;
//
//
//import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
//import org.apache.camel.Converter;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//
//@Converter
//public final class CustomTypeConverters {
//
//    @Converter
//    public static OpenTicketsResponse toOpenTicketsResponse(String json) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(json, OpenTicketsResponse.class);
//    }
//}