package cn.saatana.module.system.file.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.file.entity.Resource;
import cn.saatana.module.system.file.repository.ResourceRepository;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends CurdService<ResourceRepository, Resource> {

}
