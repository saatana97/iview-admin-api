package cn.saatana.core.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonRepository<Entity extends CommonEntity> extends JpaRepository<Entity, Integer> {
}
