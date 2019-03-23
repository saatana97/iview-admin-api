package cn.saatana.core.org.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.org.entity.Org;
import cn.saatana.core.org.repository.OrgRepository;
import cn.saatana.core.org.service.OrgService;
import cn.saatana.core.role.entity.Role;
import cn.saatana.core.role.service.RoleService;
import cn.saatana.core.utils.TreeUtils;
import cn.saatana.core.utils.tree.TreeNode;

@RestController
@RequestMapping("/org")
@LogOparetion("组织机构管理")
public class OrgController extends CommonController<OrgService, OrgRepository, Org> {
	@Autowired
	private RoleService roleService;

	@RequestMapping("tree")
	public Res<List<TreeNode<Org>>> tree() {
		List<Org> all = service.findList(new Org());
		List<TreeNode<Org>> tree = TreeUtils.buildTree(all);
		return Res.ok(tree);
	}

	@RequestMapping("tree/{roleId}")
	public Res<List<TreeNode<Org>>> roleMenuTree(@PathVariable(required = true) Integer roleId) {
		List<TreeNode<Org>> data = tree().getData();
		Set<Integer> scopes = new HashSet<>();
		Role role = roleService.get(roleId);
		if (role != null) {
			scopes.addAll(role.getAccessScopes());
		}
		if (scopes != null) {
			TreeUtils.forEachTree(data, (parent, child) -> {
				child.setChecked(scopes.contains(child.getData().getId()));
				// if (child.getChildren() == null || child.getChildren().size() == 0) {
				// }
			});
		}
		return Res.ok(data);
	}
}