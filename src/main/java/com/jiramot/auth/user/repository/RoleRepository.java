package com.jiramot.auth.user.repository;

import com.jiramot.auth.user.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
  Role findByName(String name);
}
