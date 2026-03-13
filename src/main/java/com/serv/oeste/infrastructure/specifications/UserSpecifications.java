package com.serv.oeste.infrastructure.specifications;

import com.serv.oeste.domain.enums.Roles;
import com.serv.oeste.infrastructure.entities.user.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<UserEntity> isNotAdmin() {
        return (root, query, cb) -> cb.notEqual(root.get("role"), Roles.ADMIN);
    }
}
