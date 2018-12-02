package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.Cabin;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cabin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinRepository extends JpaRepository<Cabin, Long> {

}
