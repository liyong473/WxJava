package me.chanjar.weixin.common.util.json;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * WxDateTypeAdapter 的单元测试.
 *
 * @author liyong
 */
public class WxDateTypeAdapterTest {

  private final WxDateTypeAdapter adapter = new WxDateTypeAdapter();

  private Date read(String json) throws IOException {
    JsonReader reader = new JsonReader(new StringReader(json));
    reader.setLenient(true);
    return adapter.read(reader);
  }

  private String write(Date date) throws IOException {
    StringWriter out = new StringWriter();
    JsonWriter writer = new JsonWriter(out);
    writer.setLenient(true);
    adapter.write(writer, date);
    return out.toString();
  }

  /**
   * 秒级时间戳转成毫秒时如果按 int 运算会溢出，导致解析出的时间错误（甚至早于 1970 年）。
   */
  @Test
  public void testReadCurrentTimestamp() throws IOException {
    long seconds = 1481013459L;
    Date date = read(String.valueOf(seconds));
    assertThat(date).isNotNull();
    assertThat(date.getTime()).isEqualTo(seconds * 1000L);
  }

  @Test
  public void testReadTimestampAfterYear2038() throws IOException {
    long seconds = 4102444800L;
    Date date = read(String.valueOf(seconds));
    assertThat(date).isNotNull();
    assertThat(date.getTime()).isEqualTo(seconds * 1000L);
  }

  @Test
  public void testReadNull() throws IOException {
    assertThat(read("null")).isNull();
  }

  @Test
  public void testWrite() throws IOException {
    long seconds = 1481013459L;
    assertThat(write(new Date(seconds * 1000L))).isEqualTo(String.valueOf(seconds));
    assertThat(write(null)).isEqualTo("null");
  }

  /**
   * 序列化与反序列化应当可以互相还原。
   */
  @Test
  public void testWriteThenRead() throws IOException {
    Date now = new Date(1600000000L * 1000L);
    assertThat(read(write(now))).isEqualTo(now);
  }
}
