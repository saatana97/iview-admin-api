package cn.saatana.config;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

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
		 Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new
		 Jackson2JsonRedisSerializer<>(
		 Object.class);
		 ObjectMapper om = new ObjectMapper();
		 om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		 om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		 jackson2JsonRedisSerializer.setObjectMapper(om);
		 template.setValueSerializer(jackson2JsonRedisSerializer);
//		FastJsonRedisSerializer<Object> json = new FastJsonRedisSerializer<>(Object.class);
//		FastJsonConfig config = new FastJsonConfig();
//		config.setFeatures(Feature.DisableCircularReferenceDetect);
//		json.setFastJsonConfig(config);
//		template.setValueSerializer(json);
//		template.afterPropertiesSet();
		return template;
	}
}
