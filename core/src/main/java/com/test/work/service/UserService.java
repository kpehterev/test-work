package main.java.com.test.work.service;

import com.test.work.entity.UserEntity;
import com.test.work.exception.ErrorResponseException;
import com.test.work.mapper.PageMapper;
import com.test.work.mapper.UserMapper;
import com.test.work.model.PageUser;
import com.test.work.model.User;
import com.test.work.model.UserCreateRequest;
import com.test.work.model.UserUpdateRequest;
import com.test.work.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.test.work.exception.exceptionfactory.NotFoundExceptionFactory.generateUserNotFoundException;


@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<UserRepository, UserEntity, Long> {

    private final UserRepository repository;

    @Override
    protected UserRepository getRepository() {
        return this.repository;
    }

    private final RedisTemplate<String, User> redisTemplate;

    private final UserMapper mapper;

    private final AccountService accountService;

    private final EmailService emailService;

    private final PhoneService phoneService;

    private final PageMapper pageMapper;

    @Transactional
    public User createUser(UserCreateRequest createRequest) {
        UserEntity user = mapper.toEntity(createRequest);
        var entity = repository.save(user);
        accountService.createAndSaveAccount(entity, createRequest.getInitialBalance());
        emailService.createAndSaveEmails(entity, createRequest.getEmails());
        phoneService.createAndSavePhones(entity, createRequest.getPhones());
        return mapper.toDto(save(user));
    }


    @Transactional
    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) throws ErrorResponseException {
        UserEntity userEntity = repository.findById(userId)
                .orElseThrow(() -> generateUserNotFoundException(userId));

        if (userUpdateRequest.getEmails() != null) {
            emailService.updateEmails(userEntity, userUpdateRequest.getEmails());
        }

        if (userUpdateRequest.getPhones() != null) {
            phoneService.updatePhones(userEntity, userUpdateRequest.getPhones());
        }

        return mapper.toDto(save(userEntity));
    }

    @Transactional(readOnly = true)
    public PageUser findUsers(LocalDate dateOfBirth, String phone, String name, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> result = repository.findUsers(dateOfBirth, phone, name, email, pageable).map(mapper::toDto);
        return pageMapper.toDto(result);
    }
}