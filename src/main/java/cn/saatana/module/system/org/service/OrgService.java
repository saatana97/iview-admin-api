package cn.saatana.module.system.org.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.org.entity.Org;
import cn.saatana.module.system.org.repository.OrgRepository;
import org.springframework.stereotype.Service;

@Service
public class OrgService extends CurdService<OrgRepository, Org> {

}
