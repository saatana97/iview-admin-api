package cn.saatana.core.utils.tree;

import java.util.Collection;

public interface Treeable<T> {
	TreeNode<T> convertToTreeNode();

	void formatChildren(Collection<T> children);

	String uniqueCode();
}
