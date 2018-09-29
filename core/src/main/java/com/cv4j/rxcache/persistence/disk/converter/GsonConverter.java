package com.cv4j.rxcache.persistence.disk.converter;

import com.google.gson.Gson;
import com.safframework.tony.common.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/29.
 */
public class GsonConverter implements Converter {

    private Gson gson;

    public GsonConverter() {

        gson = new Gson();
    }

    @Override
    public <T> T read(InputStream source, Type type) {

        String json = null;
        try {
            json = IOUtils.inputStream2String(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(json, type);
    }

    @Override
    public void writer(OutputStream sink, Object data) {

        String wrapperJSONSerialized = gson.toJson(data);
        byte[] buffer = wrapperJSONSerialized.getBytes();

        try {
            sink.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
