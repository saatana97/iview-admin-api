package cn.saatana.core.controller;

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
import javax.websocket.server.PathParam;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import org.springframework.web.bind.annotation.RequestParam;

import cn.saatana.core.Safer;
import cn.saatana.core.entity.CommonEntity;
import cn.saatana.core.entity.Res;
import cn.saatana.core.entity.User;
import cn.saatana.core.repository.CommonRepository;
import cn.saatana.core.service.CommonService;

public class CommonController<Service extends CommonService<Repository, Entity>, Repository extends CommonRepository<Entity>, Entity extends CommonEntity> {
	protected static final Logger log = Logger.getLogger("CommonController");
	@Autowired
	protected Service service;

	/**
	 * 获取当前登录用户
	 *
	 * @return
	 */
	protected User currentUser() {
		return Safer.currentUserInfo().getUser();
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

	@PostMapping("page")
	public Res<Page<Entity>> page(Entity entity, @RequestParam(required = false, defaultValue = "1") int index,
			@RequestParam(required = false, defaultValue = "10") int limit) {
		return Res.ok(service.findPage(entity, index, limit));
	}

	@PostMapping("get/{id}")
	public Res<Entity> get(@PathVariable Integer id) {
		return Res.ok(service.get(id));
	}

	@RequestMapping("list")
	public Res<List<Entity>> list(Entity entity) {
		return Res.ok(service.findList(entity));
	}

	@RequestMapping("all")
	public Res<List<Entity>> all() {
		return Res.ok(service.findAll());
	}

	@PostMapping("create")
	public Res<Entity> create(@Validated Entity entity, BindingResult result) throws UnsupportedEncodingException {
		validationHandler(result);
		service.create(entity);
		return Res.ok(entity);
	}

	@PostMapping("update")
	public Res<Entity> update(@Validated Entity entity, BindingResult result) {
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

	@PostMapping("remove/{id}")
	public Res<Entity> remove(@PathVariable Integer id) {
		Entity entity = service.get(id);
		service.remove(entity);
		return Res.ok(entity);
	}

	@PostMapping("removeAll")
	public Res<List<Entity>> removeAll(@RequestBody List<Integer> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.removeAll(list);
		return Res.ok(list);
	}

	@PostMapping("restore/{id}")
	public Res<Entity> restore(@PathVariable Integer id) {
		Entity entity = service.get(id);
		service.restore(entity);
		return Res.ok(entity);
	}

	@PostMapping("restoreAll")
	public Res<List<Entity>> restore(@RequestBody List<Integer> idList) {
		List<Entity> list = service.findAllByIds(idList);
		service.restoreAll(list);
		return Res.ok(list);
	}
}
