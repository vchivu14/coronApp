package cvb.capp.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectURL = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        label:
        for (GrantedAuthority grantedAuthority : authorities) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_USER":
                    redirectURL = "/user/"+request.getParameter("username");
                    break label;
                case "ROLE_ADMIN":
                    redirectURL = "/admin/"+request.getParameter("username");
                    break label;
                case "ROLE_SECRETARY":
                    redirectURL = "/secretary/"+request.getParameter("username");
                    break label;
            }
        }
        if (redirectURL == null) {
            throw new IllegalStateException();
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectURL);
    }
}
