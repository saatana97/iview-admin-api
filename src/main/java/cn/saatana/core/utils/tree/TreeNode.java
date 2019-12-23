package cn.saatana.core.utils.tree;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String title;
	private T data;
	private int sort;
	private String parent;
	private boolean expand;
	private boolean disabled;
	private boolean disableCheckbox;
	private boolean selected;
	private boolean checked;
	private List<TreeNode<T>> children = new ArrayList<>();

	@Deprecated
	public TreeNode() {
		this(null, "未命名", null);
	}

	public TreeNode(String code, String title, T data) {
		this(code, title, null, data);
	}

	public TreeNode(String code, String title, String parent, T data) {
		this(code, title, parent, data, 0, true, false);
	}

	public TreeNode(String code, String title, String parent, T data, int sort, boolean expand, boolean disabled) {
		this.code = code;
		this.title = title;
		this.parent = parent;
		this.data = data;
		this.sort = sort;
		this.expand = expand;
		this.disabled = disabled;
	}
}
