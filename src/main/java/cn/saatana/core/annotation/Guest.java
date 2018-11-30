package cn.saatana.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记不用登陆也能访问，可以标记整个控制器也可以只标记某个接口
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Guest {
	/**
	 * 是否不需要登陆权限，默认true
	 * 
	 * @return
	 */
	boolean value() default true;
}
