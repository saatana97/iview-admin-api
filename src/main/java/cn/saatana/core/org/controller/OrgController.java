package cn.saatana.core.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.org.entity.Org;
import cn.saatana.core.org.repository.OrgRepository;
import cn.saatana.core.org.service.OrgService;

@RestController
@RequestMapping("/org")
@LogOparetion("组织机构管理")
public class OrgController extends CommonController<OrgService, OrgRepository, Org> {
	@RequestMapping("tree")
	public Res<List<Org>> tree() {
		// TODO 将查询全部组织机构改为查询当前用户有权限查看的组织机构
		List<Org> all = service.findAll();
		Map<Integer, Org> map = new HashMap<>();
		all.forEach(item -> {
			map.put(item.getId(), item);
		});
		List<Integer> childrenId = new ArrayList<>();
		all.forEach(item -> {
			Org parent = item.getParent();
			if (parent != null) {
				parent.getChildren().add(item);
				childrenId.add(item.getId());
			}
		});
		List<Org> root = new ArrayList<>();
		map.forEach((key, value) -> {
			if (!childrenId.contains(key)) {
				root.add(value);
			}
		});
		return Res.ok(root);
	}
}
