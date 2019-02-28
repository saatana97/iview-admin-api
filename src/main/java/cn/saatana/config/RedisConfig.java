package cn.saatana.config;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

	/**
	 * 生成key的策略
	 *
	 * @return
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * 管理缓存
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate<Serializable, Object> redisTemplate) {
		RedisCacheManager rcm = RedisCacheManager.builder(redisTemplate.getConnectionFactory()).build();
		return rcm;
	}

	/**
	 * RedisTemplate配置
	 */
	@Bean
	public RedisTemplate<Serializable, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		FastJsonRedisSerializer<Object> json = new FastJsonRedisSerializer<>(Object.class);
		FastJsonConfig config = new FastJsonConfig();
		config.setFeatures(Feature.DisableCircularReferenceDetect);
		config.setCharset(Charset.forName("UTF-8"));
		json.setFastJsonConfig(config);
		template.setValueSerializer(json);
		template.afterPropertiesSet();
		return template;
	}
}
