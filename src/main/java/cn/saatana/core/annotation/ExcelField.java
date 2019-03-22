package cn.saatana.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记导入导出时使用的字段，可以标记一个字段也可以标记字段对应的get方法
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
	/**
	 * 列标题
	 *
	 * @return
	 */
	String title();

	/**
	 * 排序
	 *
	 * @return
	 */
	int sort() default 0;

	/**
	 * 所属分组
	 *
	 * @return
	 */
	int[] groups() default {};

	/**
	 * 引用字典类型
	 *
	 * @return
	 */
	String dictType() default "";

	/**
	 * 日期格式
	 *
	 * @return
	 */
	String dateFormat() default "yyyy-MM-dd HH:mm:ss";

}
