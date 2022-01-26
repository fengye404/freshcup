package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: 風楪fy
 * @create: 2022-01-27 01:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TraceLog {
    /**
     * 操作描述
     */
    private String description;
    /**
     * 请求平台
     */
    private String env;
    /**
     * UA
     */
    private String userAgent;
    /**
     * 操作用户
     */
    private Object user;

    /**
     * 消耗时间
     */
    private String spendTime;

    /**
     * URL
     */
    private String url;

    /**
     * 请求参数
     */
    private Object params;

    /**
     * 请求返回的结果
     */
    private Object result;


    public String toLogFormat(Boolean requestStatus) {
        String strResult = getResult(200);
        String strParam = String.valueOf(params);
        String format = requestStatus ? commonFormat : errorFormat;
        String strUser = String.valueOf(user);
        return String.format(format, description, env, url, strParam, strResult, strUser, spendTime, userAgent);
    }

    /**
     * 截取指定长度的返回值
     *
     * @param factor
     * @return
     */
    public String getResult(int factor) {
        String result = String.valueOf(this.result);
        if (factor == 0 || result.length() < factor) {
            return result;
        }
        return result.substring(0, factor - 1) + "...}";
    }

    private final static String commonFormat = "\n===========捕获响应===========\n" +
            "操作描述：%s\n" +
            "请求平台：%s\n" +
            "请求地址：%s\n" +
            "请求参数：%s\n" +
            "请求返回：%s\n" +
            "请求用户：%s\n" +
            "请求耗时：%s\n" +
            "请求UA：%s\n" +
            "===========释放响应===========";
    private final static String errorFormat = "\n===========捕获异常===========\n" +
            "操作描述：%s\n" +
            "请求平台：%s\n" +
            "请求地址：%s\n" +
            "请求参数：%s\n" +
            "请求异常：%s\n" +
            "请求用户：%s\n" +
            "请求耗时：%s\n" +
            "请求UA：%s\n" +
            "===========释放异常===========";

}
