package com.fa.ims.repository;

import com.fa.ims.entity.Role;
import com.fa.ims.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserListRepository extends JpaRepository<User, Long> {


    Integer countUserByUserNameStartsWith(String username);

    List<User> findAllByUserEmail(String email);

    List<User> findAllByDeletedIsFalse();

    Page<User> findAllByDeletedIsFalse(Pageable pageable);

    Optional<User> findUserByUserEmail(String email);

    List<User> findAllByUserRole_RoleDesc(String role_desc);

    User findUserById(Long userId);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN u.userRole r " +
            "WHERE (CONCAT(u.userName,' ',u.userEmail,' ', u.userPhone) LIKE %:keyword%) " +
            "AND (:role = '' OR r.roleDesc = :role)" +
            "AND u.deleted = false")
    Page<User> search(@Param("keyword") String keyword, @Param("role") String role, Pageable pageable);
}
