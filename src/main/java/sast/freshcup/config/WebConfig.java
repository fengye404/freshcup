package sast.freshcup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sast.freshcup.interceptor.AccountInterceptor;
import sast.freshcup.interceptor.RequestInterceptor;

/**
 * @author: 風楪fy
 * @create: 2022-01-20 01:29
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccountInterceptor accountInterceptor;

    @Autowired
    private RequestInterceptor requestInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(accountInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login", "/getValidateCode");
//
//        registry.addInterceptor(requestInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login", "/getValidateCode");
    }

}
