package net.skhu.repository;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.skhu.domain.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserNumAndPassword(int userNum, String password);
	User findByUserNum(int userNum);
	User findByEmail(String email);
	User findByAuthKey(String authKey);
	User findByNameContaining(String text);
	
	//타임라인을 확인한 시간
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET timelineTime = :time WHERE userId = :id")
    void updateUserTimelineTime(@Param("id") int id, @Param("time") LocalDateTime time);	
}
