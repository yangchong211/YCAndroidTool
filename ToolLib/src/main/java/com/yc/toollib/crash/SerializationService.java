package com.yc.toollib.crash;

import java.lang.reflect.Type;

@Deprecated
public interface SerializationService {

    @Deprecated
    <T> T json2Object(String input, Class<T> clazz);

    String object2Json(Object instance);

    <T> T parseObject(String input, Type clazz);

}
