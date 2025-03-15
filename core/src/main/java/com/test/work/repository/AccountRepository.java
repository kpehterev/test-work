package com.test.work.repository;

import com.test.work.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends CrudRepository<AccountEntity> {
}