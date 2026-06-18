package com.etiya.elearning.dataAccess.abstracts;

import com.etiya.elearning.entities.concretes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByMail(String mail);

    boolean existsByMailAndIdNot(String mail, Long id);

    Optional<User> findByMail(String mail);

    boolean existsByRole_Id(Long roleId);
}
