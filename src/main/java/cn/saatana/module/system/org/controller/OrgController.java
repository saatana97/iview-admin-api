package cn.saatana.module.system.org.controller;

import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.org.entity.Org;
import cn.saatana.module.system.org.repository.OrgRepository;
import cn.saatana.module.system.org.service.OrgService;
import cn.saatana.core.utils.TreeUtils;
import cn.saatana.core.utils.tree.TreeNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/org")
public class OrgController extends CurdController<OrgService, OrgRepository, Org> {

	@RequestMapping("tree")
	public Res<List<TreeNode<Org>>> tree() {
		List<Org> all = service.findList(new Org());
		List<TreeNode<Org>> tree = TreeUtils.buildTree(all);
		return Res.ok(tree);
	}

}
