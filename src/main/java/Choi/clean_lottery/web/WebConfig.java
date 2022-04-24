package Choi.clean_lottery.web;

import Choi.clean_lottery.web.converter.LocalDateToStringConverter;
import Choi.clean_lottery.web.converter.StringToLocalDateConverter;
import Choi.clean_lottery.web.interceptor.LogInterceptor;
import Choi.clean_lottery.web.interceptor.LoginCheckInterceptor;
import Choi.clean_lottery.web.interceptor.MemberCheckInterceptor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String APP_DOMAIN = "http://mil.malclean.kr";
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

    @Bean
    public ServletContextInitializer clearJsession() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
                SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
            }
        };
    }
}
