package cn.saatana.core.dict.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.dict.entity.Dictionary;
import cn.saatana.core.dict.repository.DictionaryRepository;
import cn.saatana.core.dict.service.DictionaryService;

@RestController
@LogOparetion("字典管理")
@RequestMapping("/dict")
public class DictionaryController extends CommonController<DictionaryService, DictionaryRepository, Dictionary> {

}
