package org.example.javademobot.service;

import org.example.javademobot.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean registerUser(Message msg);

    User save(User product);

    List<User> findAll();

    Optional<User> findById(Long id);

    void deleteById(Long id);
}
