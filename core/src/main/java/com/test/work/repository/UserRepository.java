package main.java.com.test.work.repository;

import com.test.work.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends CrudRepository<UserEntity> {

    @Query("SELECT u FROM UserEntity u " +
            "WHERE (:dateOfBirth IS NULL OR u.dateOfBirth > :dateOfBirth) " +
            "AND (:phone IS NULL OR u.phone = :phone) " +
            "AND (:name IS NULL OR u.name LIKE :name%) " +
            "AND (:email IS NULL OR u.email = :email)")
    Page<UserEntity> findUsers(
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("phone") String phone,
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable
    );
}