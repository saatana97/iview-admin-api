package cn.saatana.core.dict.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import cn.saatana.core.common.CommonService;
import cn.saatana.core.dict.entity.Dictionary;
import cn.saatana.core.dict.repository.DictionaryRepository;

@Service
public class DictionaryService extends CommonService<DictionaryRepository, Dictionary> {

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
