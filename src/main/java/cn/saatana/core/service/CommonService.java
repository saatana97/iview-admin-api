package cn.saatana.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.saatana.core.entity.CommonEntity;
import cn.saatana.core.repository.CommonRepository;

@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = false)
public class CommonService<Repository extends CommonRepository<Entity>, Entity extends CommonEntity> {
	@Autowired
	protected Repository repository;

	/**
	 * 根据ID查询数据
	 *
	 * @param id
	 *            唯一标识ID
	 * @return 实体
	 */
	@Transactional(readOnly = true)
	public Entity get(Integer id) {
		return repository.getOne(id);
	}

	/**
	 * 根据条件查询分页数据，默认所有条件并联，String类型属性模糊查询，忽略空值属性，排序按最后修改时间/创建时间降序排列，只查询数据状态正常（STATUS_NORMAL）的数据
	 *
	 * @see 如需自定义可重写本方法
	 *
	 * @param entity
	 *            查询条件
	 * @param index
	 *            页码
	 * @param limit
	 *            页容量
	 * @return 分页数据
	 */
	@Transactional(readOnly = true)
	public Page<Entity> findPage(Entity entity, int index, int limit) {
		return repository.findAll(
				Example.of(entity,
						ExampleMatcher.matchingAll().withIgnoreNullValues()
								.withStringMatcher(StringMatcher.CONTAINING)),
				PageRequest.of(index - 1, limit, Direction.DESC, "updateDate", "createDate"));
	}

	/**
	 * 查询所有数据，包括已删除的
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Entity> findAll() {
		return repository.findAll();
	}

	/**
	 * 根据条件查询数据，默认只查询数据状态正常（STATUS_NORMAL）的数据
	 *
	 * @param entity
	 *            查询条件
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Entity> findList(Entity entity) {
		return repository.findAll(Example.of(entity,
				ExampleMatcher.matchingAll().withIgnoreNullValues().withStringMatcher(StringMatcher.CONTAINING)));
	}

	/**
	 * 根据ID集合查询数据集合
	 *
	 * @param ids
	 *            唯一标识ID集合
	 * @return 实体集合
	 */
	public List<Entity> findAllByIds(Iterable<Integer> ids) {
		return repository.findAllById(ids);
	}

	/**
	 * 创建一条数据，自动填充创建人、创建时间
	 *
	 * @param entity
	 *            实体
	 */
	public void create(Entity entity) {
		if (entity != null) {
			entity.preCreate();
			repository.saveAndFlush(entity);
		}
	}

	/**
	 * 批量创建数据，自动填充创建人、创建时间
	 *
	 * @param entities
	 *            实体集合
	 */
	public void createAll(Iterable<Entity> entities) {
		if (entities != null) {
			entities.forEach(entity -> {
				entity.preCreate();
			});
			repository.saveAll(entities);
			repository.flush();
		}
	}

	/**
	 * 修改数据，自动填充最后修改人、最后修改时间
	 *
	 * @param entity
	 *            实体
	 */
	public void update(Entity entity) {
		update(entity, true);
	}

	/**
	 * 修改数据，自动填充最后修改人、最后修改时间
	 *
	 * @param entity
	 *            实体
	 * @param updateUpdateDate
	 *            是否更新最后更新时间
	 */
	public void update(Entity entity, boolean updateUpdateDate) {
		if (entity != null) {
			if (updateUpdateDate) {
				entity.preUpdate();
			}
			repository.saveAndFlush(entity);
		}
	}

	/**
	 * 批量修改数据，自动填充最后修改人、最后修改时间
	 *
	 * @param entities
	 *            实体集合
	 */
	public void updateAll(Iterable<Entity> entities) {
		if (entities != null) {
			entities.forEach(entity -> {
				entity.preUpdate();
			});
			repository.saveAll(entities);
			repository.flush();
		}
	}

	/**
	 * 逻辑删除数据，将数据状态改为删除（STATUS_DELETED），自动填充最后修改人、最后修改时间
	 *
	 * @param entity
	 *            实体
	 */
	public void remove(Entity entity) {
		if (entity != null) {
			entity.preDelete();
			repository.saveAndFlush(entity);
		}
	}

	/**
	 * 批量逻辑删除数据，将数据状态改为删除（STATUS_DELETED），自动填充最后修改人、最后修改时间
	 *
	 * @param entities
	 */
	public void removeAll(Iterable<Entity> entities) {
		if (entities != null) {
			entities.forEach(entity -> {
				entity.preDelete();
			});
			repository.saveAll(entities);
			repository.flush();
		}
	}

	/**
	 * 恢复数据，将数据状态改为正常（STATUS_NORMAL），自动填充最后修改人、最后修改时间
	 *
	 * @param entity
	 *            实体
	 */
	public void restore(Entity entity) {
		if (entity != null) {
			entity.preRestore();
			repository.saveAndFlush(entity);
		}
	}

	/**
	 * 批量恢复数据，将数据状态改为正常（STATUS_NORMAL），自动填充最后修改人、最后修改时间
	 *
	 * @param entities
	 *            实体集合
	 */
	public void restoreAll(Iterable<Entity> entities) {
		if (entities != null) {
			entities.forEach(entity -> {
				entity.preRestore();
			});
			repository.saveAll(entities);
			repository.flush();
		}
	}
}
