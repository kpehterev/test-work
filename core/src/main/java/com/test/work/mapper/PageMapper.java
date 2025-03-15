package main.java.com.test.work.mapper;

import com.test.work.model.PageUser;
import com.test.work.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageMapper {

    private final ModelMapper modelMapper;

    public PageUser toDto(Page<User> userPage) {
        PageUser pageUser = new PageUser();
        pageUser.setContent(userPage.getContent());
        pageUser.setNumber(userPage.getNumber());
        pageUser.setSize(userPage.getSize());
        pageUser.setTotalPages(userPage.getTotalPages());
        pageUser.setTotalElements((int) userPage.getTotalElements());
        pageUser.setLast(userPage.isLast());
        pageUser.setFirst(userPage.isFirst());
        pageUser.setEmpty(userPage.isEmpty());
        return pageUser;
    }
}
