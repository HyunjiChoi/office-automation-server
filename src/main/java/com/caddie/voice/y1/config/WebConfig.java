package com.caddie.voice.y1.config;

import com.caddie.voice.y1.handler.AuthorizeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizeInterceptor pathLogInterceptor;

    public WebConfig(AuthorizeInterceptor pathLogInterceptor) {
        this.pathLogInterceptor = pathLogInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pathLogInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/webjars/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /*@Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(10000); // 연결시간 초과
        return new RestTemplate(httpRequestFactory);
    }*/
}
