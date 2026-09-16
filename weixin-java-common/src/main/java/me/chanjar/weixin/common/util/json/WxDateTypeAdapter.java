package me.chanjar.weixin.common.util.json;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * <pre>
 * Gson 日期类型转换器
 * Created by Binary Wang on 2017-7-8.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxDateTypeAdapter extends TypeAdapter<Date> {
  @Override
  public void write(JsonWriter out, Date value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.getTime() / 1000);
    }
  }

  @Override
  public Date read(JsonReader in) throws IOException {
    JsonToken peek = in.peek();
    switch (peek) {
      case NULL:
        in.nextNull();
        return null;
      case NUMBER:
        // 微信返回的是秒级时间戳，需转为毫秒；此处必须用 long 读取并运算，
        // 否则 in.nextInt() * 1000 会发生 int 溢出，导致解析出错误的时间
        return new Date(in.nextLong() * 1000L);
      default:
        throw new JsonParseException("Expected NUMBER but was " + peek);
    }
  }
}
