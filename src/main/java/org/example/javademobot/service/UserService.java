package org.example.javademobot.service;

import org.example.javademobot.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

public interface UserService {
    boolean registerUser(Message msg);

    User save(User product);

    List<User> findAll();

    User findById(Long id);

    void deleteById(Long id);
}
