package cn.saatana.core.org.service;

import org.springframework.stereotype.Service;

import cn.saatana.core.common.CommonService;
import cn.saatana.core.org.entity.Org;
import cn.saatana.core.org.repository.OrgRepository;

@Service
public class OrgService extends CommonService<OrgRepository, Org> {

}
