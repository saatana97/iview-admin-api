package cn.saatana.core.log.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.Admin;
import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.log.entity.OparetionLog;
import cn.saatana.core.log.repository.OparetionLogRepository;
import cn.saatana.core.log.service.OparetionLogService;

@Admin
@RestController
@LogOparetion("操作日志管理")
@RequestMapping("/log")
public class OparetionLogController
		extends CommonController<OparetionLogService, OparetionLogRepository, OparetionLog> {

	@Override
	@Admin(false)
	@PostMapping("page")
	@LogOparetion(value = "", ignore = true)
	public Res<Page<OparetionLog>> page(@RequestBody OparetionLog entity) {
		return super.page(entity);
	}

}
