package cn.saatana.core.common;

import cn.saatana.config.AppProperties;
import cn.saatana.config.TextProperties;
import cn.saatana.core.Safer;
import cn.saatana.module.system.user.entity.UserInfo;
import cn.saatana.module.system.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Api("基础控制器")
@ControllerAdvice
public abstract class CurdController<Service extends CurdService<Repository, Entity>, Repository extends CurdRepository<Entity>, Entity extends BaseEntity> {
	protected static final Logger log = Logger.getLogger("CommonController");
	@Autowired
	protected Service service;
	@Autowired
	protected AppProperties appProp;
	@Autowired
	protected TextProperties textProp;

	@ApiOperation("分页查询")
	@PostMapping("page")
	public Res<Page<Entity>> page(@RequestBody Entity entity) {
		return Res.ok(service.findPage(entity));
	}

	@ApiOperation("主键查询")
	@PostMapping("get/{id}")
	public Res<Entity> get(@PathVariable String id) {
		return Res.ok(service.get(id));
	}

	@ApiOperation("条件查询")
	@PostMapping("list")
	public Res<List<Entity>> list(@RequestBody Entity entity) {
		return Res.ok(service.findList(entity));
	}

	@ApiOperation("重复校验")
	@RequestMapping("check")
	public Res<List<Entity>> check(@RequestBody Entity entity) {
		return Res.ok(service.findList(entity, StringMatcher.EXACT));
	}

	@ApiOperation("列表查询")
	@RequestMapping("all")
	public Res<List<Entity>> all() {
		return Res.ok(service.findAll());
	}

	@ApiOperation("插入数据")
	@PostMapping("create")
	public Res<Entity> create(@Validated @RequestBody Entity entity, BindingResult result)
			throws UnsupportedEncodingException {
		validationHandler(result);
		service.create(entity);
		return Res.ok(entity);
	}

	@ApiOperation("更新数据")
	@PostMapping("update")
	public Res<Entity> update(@Validated @RequestBody Entity entity, BindingResult result) {
		validationHandler(result);
		Entity recoard = service.get(entity.getId());
		if (recoard != null) {
			copyNotNullProperties(entity, recoard);
			service.update(recoard);
			return Res.ok(entity);
		} else {
			return Res.error(entity);
		}
	}

	@ApiOperation("逻辑删除数据")
	@PostMapping("remove/{id}")
	public Res<Entity> remove(@PathVariable String id) {
		Entity entity = service.get(id);
		service.remove(entity);
		return Res.ok(entity);
	}

	@ApiOperation("批量逻辑删除数据")
	@PostMapping("removeAll")
	public Res<List<Entity>> removeAll(@RequestBody List<String> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.removeAll(list);
		return Res.ok(list);
	}

	@ApiOperation("逻辑恢复数据")
	@PostMapping("restore/{id}")
	public Res<Entity> restore(@PathVariable String id) {
		Entity entity = service.get(id);
		service.restore(entity);
		return Res.ok(entity);
	}

	@ApiOperation("批量逻辑恢复数据")
	@PostMapping("restoreAll")
	public Res<List<Entity>> restore(@RequestBody List<String> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.restoreAll(list);
		return Res.ok(list);
	}

	@ApiOperation("物理删除数据")
	@PostMapping("delete/{id}")
	public Res<Entity> delete(@PathVariable String id) {
		Entity entity = service.get(id);
		service.remove(entity);
		return Res.ok(entity);
	}

	@ApiOperation("批量物理删除数据")
	@PostMapping("deleteAll")
	public Res<List<Entity>> deleteAll(@RequestBody List<String> ids) {
		List<Entity> list = service.findAllByIds(ids);
		service.removeAll(list);
		return Res.ok(list);
	}

	/**
	 * 获取当前授权信息
	 *
	 * @return
	 */
	protected User currentAuth() {
		UserInfo info = Safer.currentAuthInfo();
		return info == null ? null : info.getAuth();
	}

	/**
	 * 获取当前有效token
	 *
	 * @return
	 */
	protected String currentToken() {
		return Safer.scanToken();
	}

	@ExceptionHandler(Exception.class)
	public Res<String> excaptionHandler(Exception e) {
		log.log(Level.SEVERE, e.getMessage(), e);
		return Res.error(e.getMessage());
	}

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 日志记录实体验证结果并抛出验证不通过的异常
	 *
	 * @param result
	 * @throws ValidationException
	 */
	protected void validationHandler(BindingResult result) {
		if (result.hasFieldErrors()) {
			StringBuilder sb = new StringBuilder();
			sb.append("数据格式不正确:");
			for (FieldError error : result.getFieldErrors()) {
				sb.append("【");
				sb.append(error.getField());
				sb.append("不能为");
				sb.append(error.getRejectedValue());
				sb.append("】");
			}
			throw new ValidationException(sb.toString());
		}
	}

	/**
	 * 将非空值属性从源对象复制到目标对象
	 * 包装了org.springframework.beans.BeanUtils.copyProperties(Object obj,Object obj,String... ignoreProperties)方法
	 * @param source 源对象
	 * @param target 目标对象
	 */
	protected void copyNotNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getPropertiesWithNullValue(source).toArray(new String[0]));
	}

	private List<String> getPropertiesWithNullValue(Object obj, Class<?>... classs) {
		List<String> res = new ArrayList<>();
		Class<?> objClass = classs.length > 0 ? classs[0] : obj.getClass();
		Arrays.asList(objClass.getDeclaredFields()).forEach(field -> {
			if (!isIgnoreField(field)) {
				String name = field.getName();
				String getterName = "get" + name.substring(0, 1).toUpperCase()
						+ (name.length() > 1 ? name.substring(1) : "");
				try {
					Method getter = objClass.getMethod(getterName);
					if (Modifier.isPublic(getter.getModifiers())) {
						Object value = getter.invoke(obj);
						if (value == null) {
							if(obj instanceof BaseEntity){
								BaseEntity entity = (BaseEntity) obj;
								if(entity.setToNull != null){
									if(!entity.setToNull.contains(name)){
										res.add(name);
									}
								}
							}else{
								res.add(name);
							}
						}
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					log.info(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		Class<?> superClass = objClass.getSuperclass();
		if (superClass != null) {
			// Object superObj = superClass.cast(obj);
			res.addAll(getPropertiesWithNullValue(obj, superClass));
		}
		return res;
	}

	private boolean isIgnoreField(Field field) {
		boolean res = false;
		if (field.getAnnotation(ManyToOne.class) != null) {
			res = true;
		} else if (field.getAnnotation(OneToOne.class) != null) {
			res = true;
		} else if (field.getAnnotation(Transient.class) != null) {
			res = true;
		} else if (Modifier.isStatic(field.getModifiers())) {
			res = true;
		}
		return res;
	}

}
