package com.janak.androidnetworking.jacksonparserfactory;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.janak.androidnetworking.interfaces.Parser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

final class JacksonRequestBodyParser<T> implements Parser<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final ObjectWriter adapter;

    JacksonRequestBodyParser(ObjectWriter adapter) {
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        byte[] bytes = adapter.writeValueAsBytes(value);
        return RequestBody.create(MEDIA_TYPE, bytes);
    }
}