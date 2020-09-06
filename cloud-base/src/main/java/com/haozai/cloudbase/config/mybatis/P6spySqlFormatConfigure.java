package com.haozai.cloudbase.config.mybatis;



import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义 p6spy sql输出格式
 *
 * @author haozai
 */
@Slf4j
public class P6spySqlFormatConfigure implements MessageFormattingStrategy {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * SQL分析
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ? simpleDateFormat.format(new Date())
                + " | 耗时 " + elapsed + " ms | SQL 语句：" + StringUtils.LF + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";" : StringUtils.EMPTY;
    }
}
