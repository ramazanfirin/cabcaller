package com.mastertek.cabcaller.repository;

import com.mastertek.cabcaller.domain.Stuff;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Stuff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StuffRepository extends JpaRepository<Stuff, Long> {

}
