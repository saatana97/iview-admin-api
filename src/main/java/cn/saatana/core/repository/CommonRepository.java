package cn.saatana.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.saatana.core.entity.CommonEntity;

public interface CommonRepository<Entity extends CommonEntity> extends JpaRepository<Entity, Integer> {
}
