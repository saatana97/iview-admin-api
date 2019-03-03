package cn.saatana.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识时候需要admin账户才能访问 ,可以标记整个控制器也可以只标记某一个接口
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Admin {
	/**
	 * 是否需要Admin权限，默认true
	 *
	 * @return
	 */
	boolean value() default true;
}
