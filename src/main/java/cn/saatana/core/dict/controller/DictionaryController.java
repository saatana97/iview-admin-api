package cn.saatana.core.dict.controller;

import java.util.List;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.dict.entity.Dictionary;
import cn.saatana.core.dict.repository.DictionaryRepository;
import cn.saatana.core.dict.service.DictionaryService;

@RestController
@LogOparetion("字典管理")
@RequestMapping("/dict")
public class DictionaryController extends CommonController<DictionaryService, DictionaryRepository, Dictionary> {
	@PostMapping("query")
	@LogOparetion(ignore = true, value = "查字典")
	public Res<String> query(@RequestBody Dictionary entity) {
		String res = "未知";
		List<Dictionary> list = service.findList(entity, StringMatcher.EXACT);
		if (list.size() > 0) {
			res = list.get(0).getLabel();
		}
		return Res.ok(res);
	}
}
