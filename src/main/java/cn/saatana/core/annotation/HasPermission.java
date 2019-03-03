package cn.saatana.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识时候拥有指定权限才能访问,可以标记整个控制器也可以只标记某一个接口
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {
	/**
	 * 多个权限用,隔开
	 *
	 * @return
	 */
	String value();

	/**
	 * 当指定了多个权限时的校验逻辑
	 *
	 * @return
	 */
	PermissionLogic logic() default PermissionLogic.ANY;

	/**
	 * 多个权限校验逻辑
	 * 
	 * @author 向文可
	 *
	 */
	public static enum PermissionLogic {
		/**
		 * 全部匹配
		 */
		ALL,
		/**
		 * 匹配任意一个
		 */
		ANY
	}
}
