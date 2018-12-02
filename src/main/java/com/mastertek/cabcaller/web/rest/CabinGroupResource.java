package com.mastertek.cabcaller.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.cabcaller.domain.CabinGroup;

import com.mastertek.cabcaller.repository.CabinGroupRepository;
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
 * REST controller for managing CabinGroup.
 */
@RestController
@RequestMapping("/api")
public class CabinGroupResource {

    private final Logger log = LoggerFactory.getLogger(CabinGroupResource.class);

    private static final String ENTITY_NAME = "cabinGroup";

    private final CabinGroupRepository cabinGroupRepository;

    public CabinGroupResource(CabinGroupRepository cabinGroupRepository) {
        this.cabinGroupRepository = cabinGroupRepository;
    }

    /**
     * POST  /cabin-groups : Create a new cabinGroup.
     *
     * @param cabinGroup the cabinGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabinGroup, or with status 400 (Bad Request) if the cabinGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabin-groups")
    @Timed
    public ResponseEntity<CabinGroup> createCabinGroup(@Valid @RequestBody CabinGroup cabinGroup) throws URISyntaxException {
        log.debug("REST request to save CabinGroup : {}", cabinGroup);
        if (cabinGroup.getId() != null) {
            throw new BadRequestAlertException("A new cabinGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CabinGroup result = cabinGroupRepository.save(cabinGroup);
        return ResponseEntity.created(new URI("/api/cabin-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabin-groups : Updates an existing cabinGroup.
     *
     * @param cabinGroup the cabinGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabinGroup,
     * or with status 400 (Bad Request) if the cabinGroup is not valid,
     * or with status 500 (Internal Server Error) if the cabinGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabin-groups")
    @Timed
    public ResponseEntity<CabinGroup> updateCabinGroup(@Valid @RequestBody CabinGroup cabinGroup) throws URISyntaxException {
        log.debug("REST request to update CabinGroup : {}", cabinGroup);
        if (cabinGroup.getId() == null) {
            return createCabinGroup(cabinGroup);
        }
        CabinGroup result = cabinGroupRepository.save(cabinGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabinGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabin-groups : get all the cabinGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cabinGroups in body
     */
    @GetMapping("/cabin-groups")
    @Timed
    public List<CabinGroup> getAllCabinGroups() {
        log.debug("REST request to get all CabinGroups");
        return cabinGroupRepository.findAll();
        }

    /**
     * GET  /cabin-groups/:id : get the "id" cabinGroup.
     *
     * @param id the id of the cabinGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabinGroup, or with status 404 (Not Found)
     */
    @GetMapping("/cabin-groups/{id}")
    @Timed
    public ResponseEntity<CabinGroup> getCabinGroup(@PathVariable Long id) {
        log.debug("REST request to get CabinGroup : {}", id);
        CabinGroup cabinGroup = cabinGroupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cabinGroup));
    }

    /**
     * DELETE  /cabin-groups/:id : delete the "id" cabinGroup.
     *
     * @param id the id of the cabinGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabin-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabinGroup(@PathVariable Long id) {
        log.debug("REST request to delete CabinGroup : {}", id);
        cabinGroupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
