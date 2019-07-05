package com.advert.session;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

	public WebSecurityConfig(){
		System.err.println("类初始化成功!");
	}
	private final static String[] EXCLUDE_PATH={"/error","/login**","/users","/jquery-1.8.3.min.js","/jquery.md5.js","/cmd"};
    /**
     * 登录session key
     */
    public final static String SESSION_KEY = "user";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns(EXCLUDE_PATH);
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
        	System.err.println("预处理");
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null){
            	return true;
            }else{
            	 // 跳转登录
                String url = "/login";
                response.sendRedirect(url);
                return false;
            }
        }
        @Override
        public void postHandle(HttpServletRequest request,
        		HttpServletResponse response, Object handler,
        		ModelAndView modelAndView) throws Exception {
        	System.err.println("--------后置处理--------");
        }
        @Override
        public void afterCompletion(HttpServletRequest request,
        		HttpServletResponse response, Object handler, Exception ex)
        		throws Exception {
        	System.err.println("--------返回处理--------");
        }
    }
}