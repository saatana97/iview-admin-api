package cn.saatana.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;

import cn.saatana.core.utils.tree.TreeNode;
import cn.saatana.core.utils.tree.Treeable;

/**
 * 树结构通用工具类
 *
 * @author 向文可
 *
 */
public class TreeUtils {
	@SuppressWarnings("rawtypes")
	private static Map<Integer, List> buildTreeCache = new ConcurrentHashMap<>();
	@SuppressWarnings("rawtypes")
	private static Map<String, List> formatTreeCache = new ConcurrentHashMap<>();

	/**
	 * 根据指定的集合构建树结构<br>
	 * 实体必须实现<code>cn.saatana.core.utils.tree.Treeable</code>
	 *
	 * @param data
	 *            数据集合
	 * @return 树节点集合
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Treeable<R>, R> List<TreeNode<R>> buildTree(Collection<T> data) {
		List<TreeNode<R>> tree = new ArrayList<>();
		if (data != null) {
			int hashcode = data.hashCode();
			List<TreeNode<R>> cache = buildTreeCache.get(hashcode);
			if (cache == null) {
				Map<String, TreeNode<R>> map = new HashMap<>();
				data.forEach(item -> {
					TreeNode<R> node = item.convertToTreeNode();
					map.put(item.uniqueCode(), node);
				});
				map.values().forEach(item -> {
					if (StringUtils.isEmpty(item.getParent())) {
						tree.add(item);
					} else {
						TreeNode<R> parent = map.get(item.getParent());
						if (parent != null) {
							List<TreeNode<R>> children = parent.getChildren();
							children.add(item);
							children.sort((prev, next) -> {
								return prev.getSort() - next.getSort();
							});
							parent.setChildren(children);
						}
					}
				});
				tree.sort((prev, next) -> {
					return prev.getSort() - next.getSort();
				});
				buildTreeCache.put(hashcode, tree);
			} else {
				System.out.println("读取buildTree缓存");
				tree.addAll(cache);
			}
		}
		return tree;
	}

	/**
	 * 根据指定的集合构建树结构<br>
	 * 实体必须实现<code>cn.saatana.core.utils.tree.Treeable</code>
	 *
	 * @param data
	 *            数据集合
	 * @param format
	 *            生成返回数据的格式化函数
	 * @param setChildren
	 *            给父级节点数据设置子节点数据
	 * @return 返回指定格式的集合
	 */
	@SuppressWarnings("unchecked")
	public static <Node extends Treeable<Data>, Data, Format> List<Format> buildTree(Collection<Node> data,
			Function<TreeNode<Data>, Format> format, BiConsumer<Format, List<Format>> setChildren) {
		List<TreeNode<Data>> tree = buildTree(data);
		List<Format> res = new ArrayList<>();
		String hashcode = "" + tree.hashCode() + format.hashCode() + setChildren.hashCode();
		List<Format> cache = formatTreeCache.get(hashcode);
		if (cache == null) {
			res.addAll(formatTree(tree, format, node -> {
				return node.getChildren();
			}, setChildren));
			formatTreeCache.put(hashcode, res);
		} else {
			System.out.println("读取formatTree缓存");
			res.addAll(cache);
		}
		return res;
	}

	/**
	 * 格式化树结构数据为指定格式
	 *
	 * @param tree
	 *            树结构数据
	 * @param format
	 *            生成返回数据的格式化函数
	 * @param getChildren
	 *            树节点获取子节点集合的函数
	 * @param setChildren
	 *            给格式化后的父级节点设置子节点数据的消费者函数
	 * @return 格式化后的树结构数据
	 */
	public static <Node, Format> List<Format> formatTree(List<Node> tree, Function<Node, Format> format,
			Function<Node, List<Node>> getChildren, BiConsumer<Format, List<Format>> setChildren) {
		List<Format> res = new ArrayList<>();
		if (tree != null) {
			tree.forEach(item -> {
				Format parentFormat = format.apply(item);
				res.add(parentFormat);
				if (getChildren != null) {
					List<Node> children = getChildren.apply(item);
					List<Format> childrenFormat = formatTree(children, format, getChildren, setChildren);
					setChildren.accept(parentFormat, childrenFormat);
				}
			});
		}
		return res;
	}

	public static void main(String[] args) {
		List<Person> data = new ArrayList<>();
		data.add(new Person(1, "1", null, 108, null));
		data.add(new Person(11, "1-1", 1, 88, null));
		data.add(new Person(111, "11-1", 11, 68, null));
		data.add(new Person(1111, "111-1", 111, 48, null));
		data.add(new Person(11111, "1111-1", 1111, 28, null));
		data.add(new Person(111111, "11111-1", 11111, 8, null));
		data.add(new Person(111112, "11111-2", 11111, 6, null));

		data.add(new Person(2, "2", null, 98, null));

		data.add(new Person(3, "3", null, 60, null));
		data.add(new Person(31, "3-1", 3, 38, null));
		data.add(new Person(311, "31-1", 31, 10, null));
		data.add(new Person(32, "3-2", 3, 34, null));

		List<TreeNode<Person>> tree = TreeUtils.buildTree(data);
		TreeUtils.buildTree(data);
		System.out.println(tree);
		List<Person> personTree = TreeUtils.buildTree(data, TreeNode<Person>::getData, (parent, children) -> {
			parent.setChildren(children);
		});
		System.out.println(personTree);
		List<Map<String, Object>> mapTree = TreeUtils.buildTree(data, node -> {
			Map<String, Object> map = new HashMap<>();
			map.put("name", node.getData().getName());
			map.put("age", node.getData().getAge());
			return map;
		}, (parent, children) -> {
			parent.put("children", children);
		});
		System.out.println(mapTree);
	}
}

class Person implements Treeable<Person> {
	private int id;
	private String name;
	private Integer parent;
	private int age;
	private List<Person> children = new ArrayList<>();

	public Person() {
		super();
	}

	public Person(int id, String name, Integer parent, int age, List<Person> children) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.age = age;
		this.children = children;
	}

	@Override
	public TreeNode<Person> convertToTreeNode() {
		return new TreeNode<>(id + "", name, parent == null ? null : parent + "", this, age, false, false);
	}

	@Override
	public void formatChildren(Collection<Person> children) {
		children.addAll(children.parallelStream().collect(Collectors.toSet()));
	}

	@Override
	public String uniqueCode() {
		return id + "";
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public List<Person> getChildren() {
		return children;
	}

	public void setChildren(List<Person> children) {
		this.children = children;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}