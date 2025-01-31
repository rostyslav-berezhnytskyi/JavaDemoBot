package org.example.javademobot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.telegram.telegrambots.meta.api.objects.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
