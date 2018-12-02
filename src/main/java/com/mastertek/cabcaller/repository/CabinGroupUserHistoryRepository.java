package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.CabinGroupUserHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the CabinGroupUserHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinGroupUserHistoryRepository extends JpaRepository<CabinGroupUserHistory, Long> {
    @Query("select distinct cabin_group_user_history from CabinGroupUserHistory cabin_group_user_history left join fetch cabin_group_user_history.groupIDS left join fetch cabin_group_user_history.userIDS")
    List<CabinGroupUserHistory> findAllWithEagerRelationships();

    @Query("select cabin_group_user_history from CabinGroupUserHistory cabin_group_user_history left join fetch cabin_group_user_history.groupIDS left join fetch cabin_group_user_history.userIDS where cabin_group_user_history.id =:id")
    CabinGroupUserHistory findOneWithEagerRelationships(@Param("id") Long id);

}
