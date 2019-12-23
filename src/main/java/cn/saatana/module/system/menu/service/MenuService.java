package cn.saatana.module.system.menu.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.menu.entity.Menu;
import cn.saatana.module.system.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends CurdService<MenuRepository, Menu> {

}
