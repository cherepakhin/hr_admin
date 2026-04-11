package net.guides.springboot2.freemarker.config;

import net.guides.springboot2.freemarker.controller.PositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("add intercept");
		registry.addInterceptor(noCacheInterceptor())
				// Применяем ко всем запросам, которые возвращают View (HTML)
				.addPathPatterns("/**")
				// Исключаем статику (CSS, JS, картинки), чтобы они кэшировались для ускорения загрузки
				.excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**");
	}

	// Создаем бин нашего перехватчика
	public HandlerInterceptor noCacheInterceptor() {
		return new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
				log.info("add HandlerInterceptor");
				// Устанавливаем заголовки, запрещающие кэширование HTML-страниц
				response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "0");
				return true; // Продолжаем выполнение цепочки
			}
		};
	}
}