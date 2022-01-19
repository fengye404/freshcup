package sast.freshcup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sast.freshcup.interceptor.AccountInterceptor;

/**
 * @author: 風楪fy
 * @create: 2022-01-20 01:29
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AccountInterceptor accountInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/getValidateCode");
    }
}
