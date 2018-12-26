package cn.saatana.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志记录标识，用于标识控制器\方法记录日志时的操作描述
 *
 * @author 向文可
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOparetion {
	public String value();

	public boolean ignore() default false;
}
