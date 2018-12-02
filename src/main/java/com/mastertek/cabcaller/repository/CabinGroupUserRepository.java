package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.CabinGroupUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the CabinGroupUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinGroupUserRepository extends JpaRepository<CabinGroupUser, Long> {
    @Query("select distinct cabin_group_user from CabinGroupUser cabin_group_user left join fetch cabin_group_user.groupIDS left join fetch cabin_group_user.userIDS")
    List<CabinGroupUser> findAllWithEagerRelationships();

    @Query("select cabin_group_user from CabinGroupUser cabin_group_user left join fetch cabin_group_user.groupIDS left join fetch cabin_group_user.userIDS where cabin_group_user.id =:id")
    CabinGroupUser findOneWithEagerRelationships(@Param("id") Long id);

}
