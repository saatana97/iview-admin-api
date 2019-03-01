package cn.saatana.core.common;

import java.io.UnsupportedEncodingException;
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

import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.auth.entity.Authorizer;

public abstract class CommonController<Service extends CommonService<Repository, Entity>, Repository extends CommonRepository<Entity>, Entity extends CommonEntity> {
	protected static final Logger log = Logger.getLogger("CommonController");
	@Autowired
	protected Service service;

	@LogOparetion("分页查询")
	@PostMapping("page")
	public Res<Page<Entity>> page(@RequestBody Entity entity) {
		return Res.ok(service.findPage(entity));
	}

	@LogOparetion("主键查询")
	@PostMapping("get/{id}")
	public Res<Entity> get(@PathVariable Integer id) {
		return Res.ok(service.get(id));
	}

	@LogOparetion("条件查询")
	@PostMapping("list")
	public Res<List<Entity>> list(@RequestBody Entity entity) {
		return Res.ok(service.findList(entity));
	}

	@LogOparetion("重复校验")
	@RequestMapping("check")
	public Res<List<Entity>> check(@RequestBody Entity entity) {
		return Res.ok(service.findList(entity, StringMatcher.EXACT));
	}

	@LogOparetion("列表查询")
	@RequestMapping("all")
	public Res<List<Entity>> all() {
		return Res.ok(service.findAll());
	}

	@LogOparetion("插入数据")
	@PostMapping("create")
	public Res<Entity> create(@Validated @RequestBody Entity entity, BindingResult result)
			throws UnsupportedEncodingException {
		validationHandler(result);
		service.create(entity);
		return Res.ok(entity);
	}

	@LogOparetion("更新数据")
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

	@LogOparetion("逻辑删除数据")
	@PostMapping("remove/{id}")
	public Res<Entity> remove(@PathVariable Integer id) {
		Entity entity = service.get(id);
		service.remove(entity);
		return Res.ok(entity);
	}

	@LogOparetion("批量逻辑删除数据")
	@PostMapping("removeAll")
	public Res<List<Entity>> removeAll(@RequestBody List<Integer> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.removeAll(list);
		return Res.ok(list);
	}

	@LogOparetion("逻辑恢复数据")
	@PostMapping("restore/{id}")
	public Res<Entity> restore(@PathVariable Integer id) {
		Entity entity = service.get(id);
		service.restore(entity);
		return Res.ok(entity);
	}

	@LogOparetion("批量逻辑恢复数据")
	@PostMapping("restoreAll")
	public Res<List<Entity>> restore(@RequestBody List<Integer> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.restoreAll(list);
		return Res.ok(list);
	}

	@LogOparetion("物理删除数据")
	@PostMapping("delete/{id}")
	public Res<Entity> delete(@PathVariable Integer id) {
		Entity entity = service.get(id);
		service.remove(entity);
		return Res.ok(entity);
	}

	@LogOparetion("批量物理删除数据")
	@PostMapping("deleteAll")
	public Res<List<Entity>> deleteAll(@RequestBody List<Integer> ids) {
		List<Entity> list = service.findAllByIds(ids);
		service.removeAll(list);
		return Res.ok(list);
	}

	/**
	 * 获取当前授权信息
	 *
	 * @return
	 */
	protected Authorizer currentAuth() {
		return Safer.currentAuthInfo().getAuth();
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
	 *
	 * @see 包装了org.springframework.beans.BeanUtils.copyProperties(Object obj,Object
	 *      obj,String... ignoreProperties)方法
	 * @param source
	 *            源对象
	 * @param target
	 *            目标对象
	 */
	protected void copyNotNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getPropertiesWithNullValue(source).toArray(new String[0]));
	}

	private List<String> getPropertiesWithNullValue(Object obj, Class<?>... classs) {
		List<String> res = new ArrayList<>();
		Class<?> objClass = classs.length > 0 ? classs[0] : obj.getClass();
		Arrays.asList(objClass.getDeclaredFields()).forEach(field -> {
			String name = field.getName();
			String getterName = "get" + name.substring(0, 1).toUpperCase()
					+ (name.length() > 1 ? name.substring(1) : "");
			try {
				Method getter = objClass.getMethod(getterName);
				if (Modifier.isPublic(getter.getModifiers())) {
					Object value = getter.invoke(obj);
					if (value == null) {
						res.add(name);
					}
				}
			} catch (NoSuchMethodException | SecurityException e) {

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.info(e.getMessage());
				e.printStackTrace();
			}
		});
		Class<?> superClass = objClass.getSuperclass();
		if (superClass != null) {
			Object superObj = superClass.cast(obj);
			res.addAll(getPropertiesWithNullValue(superObj, superClass));
		}
		return res;
	}
}
