package cn.saatana.module.wechat.button.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.wechat.button.entity.Button;
import cn.saatana.module.wechat.button.repository.ButtonRepository;
import org.springframework.stereotype.Service;

@Service
public class ButtonService extends CurdService<ButtonRepository, Button> {

}
