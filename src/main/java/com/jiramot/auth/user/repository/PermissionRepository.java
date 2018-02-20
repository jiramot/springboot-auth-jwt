package com.jiramot.auth.user.repository;

import com.jiramot.auth.user.entity.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Long>{
}
