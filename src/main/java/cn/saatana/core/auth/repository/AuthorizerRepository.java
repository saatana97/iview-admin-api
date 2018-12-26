package cn.saatana.core.auth.repository;

import org.springframework.stereotype.Component;

import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.common.CommonRepository;

@Component
public interface AuthorizerRepository extends CommonRepository<Authorizer> {

	Authorizer getByUsername(String username);

}
