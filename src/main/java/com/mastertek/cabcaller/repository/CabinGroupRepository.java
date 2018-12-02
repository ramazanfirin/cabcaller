package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.CabinGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CabinGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinGroupRepository extends JpaRepository<CabinGroup, Long> {

}
