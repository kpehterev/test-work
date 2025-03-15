package main.java.com.test.work.repository;

import com.test.work.entity.PhoneDataEntity;
import com.test.work.entity.UserEntity;

import java.util.List;

public interface PhoneDataRepository extends CrudRepository<PhoneDataEntity> {

    void deleteByUserAndPhoneNotIn(UserEntity user, List<String> newPhones);

    boolean existsByPhone(String phone);
}