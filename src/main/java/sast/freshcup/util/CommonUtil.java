package sast.freshcup.util;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: 風楪fy
 * @create: 2022-01-27 01:54
 **/
public class CommonUtil {

    /**
     * 获取UA
     *
     * @param request 请求
     * @return java.lang.String
     */
    public static String getUserAgent(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(header);
        return "浏览器:" + ua.getBrowser() + " " + ua.getVersion() + "，os:" + ua.getOs() + " " + ua.getOsVersion() + "，是否移动设备:" + ua.isMobile();
    }

    /**
     * aop获取请求参数
     *
     * @param joinPoint  切点
     * @param excludeSet 排查参数set
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public static Map<String, Object> getRequestParamMap(JoinPoint joinPoint, Set<String> excludeSet) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if (excludeSet != null && excludeSet.contains(paramNames[i])) {
                continue;
            }
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }
}
