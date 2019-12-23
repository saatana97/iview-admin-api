package cn.saatana.module.system.log.controller;

import cn.saatana.core.annotation.Admin;
import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.log.entity.OparetionLog;
import cn.saatana.module.system.log.repository.OparetionLogRepository;
import cn.saatana.module.system.log.service.OparetionLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Admin
@RestController
@RequestMapping("/log")
public class OparetionLogController extends CurdController<OparetionLogService, OparetionLogRepository, OparetionLog> {

	@Override
	@Admin(false)
	@PostMapping("page")
    public Res<Page<OparetionLog>> page(@RequestBody OparetionLog entity) {
		return super.page(entity);
	}

}
