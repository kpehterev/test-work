package main.java.com.test.work.repository;

import com.test.work.entity.EmailDataEntity;
import com.test.work.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailDataRepository extends CrudRepository<EmailDataEntity> {
    void deleteByUserAndEmailNotIn(UserEntity user, List<String> newEmails);

    boolean existsByEmail(String email);
}