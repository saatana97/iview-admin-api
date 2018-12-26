package cn.saatana.core.dict.repository;

import org.springframework.stereotype.Component;

import cn.saatana.core.common.CommonRepository;
import cn.saatana.core.dict.entity.Dictionary;

@Component
public interface DictionaryRepository extends CommonRepository<Dictionary> {

}
