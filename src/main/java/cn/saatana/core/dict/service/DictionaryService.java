package cn.saatana.core.dict.service;

import org.springframework.stereotype.Service;

import cn.saatana.core.common.CommonService;
import cn.saatana.core.dict.entity.Dictionary;
import cn.saatana.core.dict.repository.DictionaryRepository;

@Service
public class DictionaryService extends CommonService<DictionaryRepository, Dictionary> {

}
