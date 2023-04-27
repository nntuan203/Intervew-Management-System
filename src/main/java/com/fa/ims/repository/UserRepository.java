package com.fa.ims.repository;

import com.fa.ims.dto.UserOfferDto;
import com.fa.ims.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUserNameAndDeletedFalse(String username);

    Optional<User> findUserByUserEmail(String email);
    @Query("SELECT new com.fa.ims.dto.UserOfferDto (u.id, u.userName) " +
            "FROM User u " +
            "INNER JOIN Role r ON u.userRole.roleId = r.roleId WHERE r.roleDesc = 'ROLE_RECRUITER'")
    List<UserOfferDto> findAllRecruiter();
    @Query("SELECT new com.fa.ims.dto.UserOfferDto (u.id, u.userName) " +
            "FROM User u " +
            "INNER JOIN Role r ON u.userRole.roleId = r.roleId WHERE r.roleDesc = 'ROLE_MANAGER'")
    List<UserOfferDto> findAllManager();

    @Query("select u from User u " +
            "where u.userRole.roleDesc = 'ROLE_INTERVIEWER' " +
            "and u.deleted = false " +
            "order by u.userFullname")
    List<User> findAllUserWhoIsInterviewer();

    @Query("select u from User u " +
            "where u.userRole.roleDesc = 'ROLE_RECRUITER' " +
            "and u.deleted = false " +
            "order by u.userFullname")
    List<User> findAllUserWhoIsRecruiter();


}
