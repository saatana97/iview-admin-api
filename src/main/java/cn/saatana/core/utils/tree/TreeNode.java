package cn.saatana.core.utils.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisableCheckbox() {
		return disableCheckbox;
	}

	public void setDisableCheckbox(boolean disableCheckbox) {
		this.disableCheckbox = disableCheckbox;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}
}
