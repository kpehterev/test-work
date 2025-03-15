package com.test.work.service;

import com.test.work.entity.PhoneDataEntity;
import com.test.work.entity.UserEntity;
import com.test.work.repository.PhoneDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneService extends AbstractService<PhoneDataRepository, PhoneDataEntity, Long> {

    private final PhoneDataRepository repository;

    @Override
    protected PhoneDataRepository getRepository() {
        return this.repository;
    }

    public void updatePhones(UserEntity user, List<String> newPhones) {
        repository.deleteByUserAndPhoneNotIn(user, newPhones);

        for (String phone : newPhones) {
            if (!repository.existsByPhone(phone)) {
                PhoneDataEntity phoneEntity = new PhoneDataEntity();
                phoneEntity.setPhone(phone);
                phoneEntity.setUser(user);
                save(phoneEntity);
            }
        }
    }

    public void createAndSavePhones(UserEntity user, List<String> phones) {
        final List<PhoneDataEntity> result = new ArrayList<>();

        for (String phone : phones) {
            PhoneDataEntity phoneData = new PhoneDataEntity();
            phoneData.setPhone(phone);
            phoneData.setUser(user);
            user.getPhones().add(phoneData);
            result.add(phoneData);
        }
        saveAll(result);
    }

}
