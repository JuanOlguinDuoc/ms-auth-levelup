package com.levelup.repository;

import com.levelup.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
	boolean existsByRun(String run);
	Optional<User> findByRun(String run);
	Optional<User> findByEmail(String email);

}
