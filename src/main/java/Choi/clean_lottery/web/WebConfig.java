package Choi.clean_lottery.web;

import Choi.clean_lottery.web.converter.LocalDateToStringConverter;
import Choi.clean_lottery.web.converter.StringToLocalDateConverter;
import Choi.clean_lottery.web.interceptor.LogInterceptor;
import Choi.clean_lottery.web.interceptor.LoginCheckInterceptor;
import Choi.clean_lottery.web.interceptor.MemberCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String APP_DOMAIN = "http://mil.malclean.kr";
//    public static final String APP_DOMAIN = "http://ec2-3-38-104-15.ap-northeast-2.compute.amazonaws.com";
//    public static final String APP_DOMAIN = "http://localhost:8080";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**");

//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/member/join", "/members/add", "/login", "/logout",
//                        "/css/**", "static/css/**", "/*.ico", "/error");

        registry.addInterceptor(new MemberCheckInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/logout", "/login", "/member/join-pass",
                        "/css/**", "/js/**", "/img/**", "/favicon.ico", "/error");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateConverter());
        registry.addConverter(new LocalDateToStringConverter());
    }
}
