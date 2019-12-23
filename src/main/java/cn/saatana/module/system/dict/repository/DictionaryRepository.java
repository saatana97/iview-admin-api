package cn.saatana.module.system.dict.repository;

import cn.saatana.core.common.CurdRepository;
import cn.saatana.module.system.dict.entity.Dictionary;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends CurdRepository<Dictionary> {

}
