package net.skhu.repository;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.skhu.domain.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserNumAndPasswordAndEmailCheckTrue(int userNum, String password);
	User findByUserNum(int userNum);
	User findByEmail(String email);
	User findByAuthKey(String authKey);
	
}
