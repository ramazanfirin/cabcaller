package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.CallerDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CallerDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CallerDetailsRepository extends JpaRepository<CallerDetails, Long> {

}
