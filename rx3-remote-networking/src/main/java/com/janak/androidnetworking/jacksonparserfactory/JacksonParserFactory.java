package com.janak.androidnetworking.jacksonparserfactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.janak.androidnetworking.interfaces.Parser;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public final class JacksonParserFactory extends Parser.Factory {

    private final ObjectMapper mapper;

    public JacksonParserFactory() {
        this.mapper = new ObjectMapper();
    }

    public JacksonParserFactory(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Parser<ResponseBody, ?> responseBodyParser(Type type) {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectReader reader = mapper.readerFor(javaType);
        return new JacksonResponseBodyParser<>(reader);
    }

    @Override
    public Parser<?, RequestBody> requestBodyParser(Type type) {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectWriter writer = mapper.writerFor(javaType);
        return new JacksonRequestBodyParser<>(writer);
    }

    @Override
    public Object getObject(String string, Type type) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            ObjectReader objectReader = mapper.readerFor(javaType);
            return objectReader.readValue(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString(Object object) {
        try {
            ObjectWriter objectWriter = mapper.writerFor(object.getClass());
            return objectWriter.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public HashMap<String, String> getStringMap(Object object) {
        try {
            TypeReference<HashMap<String, String>> typeRef
                    = new TypeReference<HashMap<String, String>>() {
            };
            ObjectWriter objectWriter = mapper.writerFor(object.getClass());
            return mapper.readValue(objectWriter.writeValueAsString(object), typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}