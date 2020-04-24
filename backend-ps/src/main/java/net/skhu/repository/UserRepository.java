package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserNumAndPasswordAndEmailCheckTrue(int userNum, String password);
}
