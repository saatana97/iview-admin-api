package cn.saatana.core.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurdRepository<Entity extends BaseEntity> extends JpaRepository<Entity, String> {
}
