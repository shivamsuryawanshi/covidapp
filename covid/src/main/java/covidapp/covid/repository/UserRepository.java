package covidapp.covid.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import covidapp.covid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import covidapp.covid.entity.User;
import java.util.Optional;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}


