package cn.saatana.module.system.dict.controller;

import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.dict.entity.Dictionary;
import cn.saatana.module.system.dict.repository.DictionaryRepository;
import cn.saatana.module.system.dict.service.DictionaryService;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictionaryController extends CurdController<DictionaryService, DictionaryRepository, Dictionary> {
	@PostMapping("query")
    public Res<String> query(@RequestBody Dictionary entity) {
		String res = "未知";
		List<Dictionary> list = service.findList(entity, StringMatcher.EXACT);
		if (list.size() > 0) {
			res = list.get(0).getLabel();
		}
		return Res.ok(res);
	}
}
