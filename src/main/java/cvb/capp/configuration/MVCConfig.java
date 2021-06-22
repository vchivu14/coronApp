package cvb.capp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MVCConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/404").setViewName("404");
        registry.addViewController("/409").setViewName("409");
        registry.addViewController("/500").setViewName("500");
        registry.addViewController("/adminPage").setViewName("adminPage");
        registry.addViewController("/secretaryPage").setViewName("secretaryPage");
        registry.addViewController("/login").setViewName("login");
    }
}
