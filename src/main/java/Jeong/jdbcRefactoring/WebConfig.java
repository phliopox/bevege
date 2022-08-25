package Jeong.jdbcRefactoring;

import Jeong.jdbcRefactoring.interceptor.AccountPwCheckInterceptor;
import Jeong.jdbcRefactoring.interceptor.AdminInterceptor;
import Jeong.jdbcRefactoring.interceptor.LoginInterceptor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements WebMvcConfigurer {

    //thymeleaf layout
    @Bean
    public LayoutDialect layoutDialect(){
        return new LayoutDialect();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico","/css/**","/image/**","/","/login","/logout"
                        ,"/error","/join-us","/js/**","/board/**","/items/**","/store/**","/images/**","/reviews/**")
                .excludePathPatterns("/not_admin");

        registry.addInterceptor(new LoginInterceptor())
                .order(2)
                .addPathPatterns("/board/new_post/**");


        registry.addInterceptor(new AccountPwCheckInterceptor())
                .order(2)
                .addPathPatterns("/my_account/**");
//                .excludePathPatterns("/my_account/point_to_money");

        registry.addInterceptor(new AdminInterceptor())
                .order(2)
                .addPathPatterns("/admin/**");
    }

}
