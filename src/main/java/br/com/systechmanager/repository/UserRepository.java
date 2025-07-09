package br.com.systechmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
	
    Optional<Users> findByUsername(String username);

    @Query(value = " select * from usuarios c where c.username = :username", nativeQuery = true)
 	Users getByUsername( @Param("username") String username );

}
