package cn.saatana.module.system.dict.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.dict.entity.Dictionary;
import cn.saatana.module.system.dict.repository.DictionaryRepository;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService extends CurdService<DictionaryRepository, Dictionary> {

	@Override
	public Page<Dictionary> findPage(Dictionary entity) {
		return repository.findAll(
				Example.of(entity,
						ExampleMatcher.matchingAll().withIgnoreNullValues()
								.withStringMatcher(StringMatcher.CONTAINING)),
				PageRequest.of(entity.getPage() - 1, entity.getLimit(), Direction.ASC, "type", "sort", "updateDate",
						"createDate"));
	}

	@Override
	public List<Dictionary> findAll() {
		return repository.findAll(Sort.by(Direction.ASC, "type", "sort", "updateDate", "createDate"));
	}

	@Override
	public List<Dictionary> findList(Dictionary entity) {
		return repository.findAll(
				Example.of(entity,
						ExampleMatcher.matchingAll().withIgnoreNullValues()
								.withStringMatcher(StringMatcher.CONTAINING)),
				Sort.by(Direction.ASC, "type", "sort", "updateDate", "createDate"));
	}

}
