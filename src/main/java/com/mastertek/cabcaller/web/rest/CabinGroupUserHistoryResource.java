package com.mastertek.cabcaller.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.cabcaller.domain.CabinGroupUserHistory;

import com.mastertek.cabcaller.repository.CabinGroupUserHistoryRepository;
import com.mastertek.cabcaller.web.rest.errors.BadRequestAlertException;
import com.mastertek.cabcaller.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CabinGroupUserHistory.
 */
@RestController
@RequestMapping("/api")
public class CabinGroupUserHistoryResource {

    private final Logger log = LoggerFactory.getLogger(CabinGroupUserHistoryResource.class);

    private static final String ENTITY_NAME = "cabinGroupUserHistory";

    private final CabinGroupUserHistoryRepository cabinGroupUserHistoryRepository;

    public CabinGroupUserHistoryResource(CabinGroupUserHistoryRepository cabinGroupUserHistoryRepository) {
        this.cabinGroupUserHistoryRepository = cabinGroupUserHistoryRepository;
    }

    /**
     * POST  /cabin-group-user-histories : Create a new cabinGroupUserHistory.
     *
     * @param cabinGroupUserHistory the cabinGroupUserHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabinGroupUserHistory, or with status 400 (Bad Request) if the cabinGroupUserHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabin-group-user-histories")
    @Timed
    public ResponseEntity<CabinGroupUserHistory> createCabinGroupUserHistory(@Valid @RequestBody CabinGroupUserHistory cabinGroupUserHistory) throws URISyntaxException {
        log.debug("REST request to save CabinGroupUserHistory : {}", cabinGroupUserHistory);
        if (cabinGroupUserHistory.getId() != null) {
            throw new BadRequestAlertException("A new cabinGroupUserHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CabinGroupUserHistory result = cabinGroupUserHistoryRepository.save(cabinGroupUserHistory);
        return ResponseEntity.created(new URI("/api/cabin-group-user-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabin-group-user-histories : Updates an existing cabinGroupUserHistory.
     *
     * @param cabinGroupUserHistory the cabinGroupUserHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabinGroupUserHistory,
     * or with status 400 (Bad Request) if the cabinGroupUserHistory is not valid,
     * or with status 500 (Internal Server Error) if the cabinGroupUserHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabin-group-user-histories")
    @Timed
    public ResponseEntity<CabinGroupUserHistory> updateCabinGroupUserHistory(@Valid @RequestBody CabinGroupUserHistory cabinGroupUserHistory) throws URISyntaxException {
        log.debug("REST request to update CabinGroupUserHistory : {}", cabinGroupUserHistory);
        if (cabinGroupUserHistory.getId() == null) {
            return createCabinGroupUserHistory(cabinGroupUserHistory);
        }
        CabinGroupUserHistory result = cabinGroupUserHistoryRepository.save(cabinGroupUserHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabinGroupUserHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabin-group-user-histories : get all the cabinGroupUserHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cabinGroupUserHistories in body
     */
    @GetMapping("/cabin-group-user-histories")
    @Timed
    public List<CabinGroupUserHistory> getAllCabinGroupUserHistories() {
        log.debug("REST request to get all CabinGroupUserHistories");
        return cabinGroupUserHistoryRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /cabin-group-user-histories/:id : get the "id" cabinGroupUserHistory.
     *
     * @param id the id of the cabinGroupUserHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabinGroupUserHistory, or with status 404 (Not Found)
     */
    @GetMapping("/cabin-group-user-histories/{id}")
    @Timed
    public ResponseEntity<CabinGroupUserHistory> getCabinGroupUserHistory(@PathVariable Long id) {
        log.debug("REST request to get CabinGroupUserHistory : {}", id);
        CabinGroupUserHistory cabinGroupUserHistory = cabinGroupUserHistoryRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cabinGroupUserHistory));
    }

    /**
     * DELETE  /cabin-group-user-histories/:id : delete the "id" cabinGroupUserHistory.
     *
     * @param id the id of the cabinGroupUserHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabin-group-user-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabinGroupUserHistory(@PathVariable Long id) {
        log.debug("REST request to delete CabinGroupUserHistory : {}", id);
        cabinGroupUserHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
