package cn.saatana.core.menu.service;

import org.springframework.stereotype.Service;

import cn.saatana.core.common.CommonService;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.menu.repository.MenuRepository;

@Service
public class MenuService extends CommonService<MenuRepository, Menu> {

}
