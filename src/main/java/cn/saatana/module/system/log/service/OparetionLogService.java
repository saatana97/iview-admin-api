package cn.saatana.module.system.log.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.log.entity.OparetionLog;
import cn.saatana.module.system.log.repository.OparetionLogRepository;
import org.springframework.stereotype.Service;

@Service
public class OparetionLogService extends CurdService<OparetionLogRepository, OparetionLog> {
}
