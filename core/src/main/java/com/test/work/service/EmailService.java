package main.java.com.test.work.service;

import com.test.work.entity.EmailDataEntity;
import com.test.work.entity.UserEntity;
import com.test.work.repository.EmailDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService extends AbstractService<EmailDataRepository, EmailDataEntity, Long> {

    private final EmailDataRepository repository;

    @Override
    protected EmailDataRepository getRepository() {
        return this.repository;
    }

    public void createAndSaveEmails(UserEntity user, Collection<String> emails) {
        List<EmailDataEntity> result = new ArrayList<>();
        for (String email : emails) {
            EmailDataEntity emailData = new EmailDataEntity();
            emailData.setEmail(email);
            emailData.setUser(user);
            result.add(emailData);
        }
        saveAll(result);
    }

    public void updateEmails(UserEntity user, List<String> newEmails) {
        repository.deleteByUserAndEmailNotIn(user, newEmails);

        for (String email : newEmails) {
            if (!repository.existsByEmail(email)) {
                EmailDataEntity emailEntity = new EmailDataEntity();
                emailEntity.setEmail(email);
                emailEntity.setUser(user);
                save(emailEntity);
            }
        }

    }
}
