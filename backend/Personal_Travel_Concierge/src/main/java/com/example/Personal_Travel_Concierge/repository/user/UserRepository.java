package com.example.Personal_Travel_Concierge.repository.user;

import com.example.Personal_Travel_Concierge.dto.userDto.UserDto;
import com.example.Personal_Travel_Concierge.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
    Optional<UserEntity> findUserByEmail(String email);
    Optional<UserEntity> findUserByUsername(String username);
    boolean existsUsernameByUsername(String username);

    @Query("SELECT com.example.Personal_Travel_Concierge.dto.userDto" +
            "(u.id, u.username, u.email," +
            " u.verificationStatus, u.fullName, u.country," +
            " u.phoneNumber, u.address, u.dateOfBirth)" +
            " FROM UserEntity u WHERE u.id = :userId")
    UserDto findUserDetailsById(long userId);


    @Query("SELECT u.id FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserIdByEmail(String email);
}