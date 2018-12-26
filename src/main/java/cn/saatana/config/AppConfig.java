package cn.saatana.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
	@Autowired
	private GlobalInterceptHandler globalInterceptHandler;
	@Autowired
	private OparetionLogInterceptHandler oparetionLogInterceptHandler;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(globalInterceptHandler);
		registry.addInterceptor(oparetionLogInterceptHandler);
	}

}
