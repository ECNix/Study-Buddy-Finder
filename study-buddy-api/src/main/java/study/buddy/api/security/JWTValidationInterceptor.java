package study.buddy.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class JWTValidationInterceptor extends HandlerInterceptorAdapter {
    JWToken jwtToken = new JWToken();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Enumeration<String> headerNames = request.getHeaderNames();

//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                System.out.println("Header: " + headerNames.nextElement());
//            }
//        }
//        System.out.println("called prehandle");
        String token = request.getHeader("Authorization");
        if(token == null) {
            return false;
        }

        return jwtToken.validate(token);
    }
}
