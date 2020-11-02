package com.janak.androidnetworking.jacksonparserfactory;

import com.fasterxml.jackson.databind.ObjectReader;
import com.janak.androidnetworking.interfaces.Parser;

import java.io.IOException;

import okhttp3.ResponseBody;

final class JacksonResponseBodyParser<T> implements Parser<ResponseBody, T> {

    private final ObjectReader adapter;

    JacksonResponseBodyParser(ObjectReader adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return adapter.readValue(value.charStream());
        } finally {
            value.close();
        }
    }

}