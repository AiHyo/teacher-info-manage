//package com.aih.common.JacksonObject;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import java.io.IOException;
//
//public class CustomDeserializer extends JsonDeserializer<Integer>{
//    @Override
//    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        boolean value = jsonParser.getBooleanValue();
//        return value ? 1 : 0;
//    }
//
//}
