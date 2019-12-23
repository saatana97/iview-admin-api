package cn.saatana.core.utils;

import cn.saatana.module.system.dict.entity.Dictionary;
import cn.saatana.module.system.dict.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictUtils {
	private static DictionaryService dictService;

	@Autowired
	public void setDictService(DictionaryService dictService) {
		DictUtils.dictService = dictService;
	}

	/**
	 * 根据字典代码和字典值查询字典标签
	 *
	 * @param code
	 *            字典代码
	 * @param value
	 *            字典值
	 * @return 字典标签
	 */
	public static String query(String code, int value) {
		String res = "未知";
		Dictionary entity = new Dictionary(code, value);
		List<Dictionary> list = dictService.findList(entity, StringMatcher.EXACT);
		if (list.size() > 0) {
			res = list.get(0).getLabel();
		}
		return res;
	}
}
