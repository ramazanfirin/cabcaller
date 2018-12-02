package com.mastertek.cabcaller.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.cabcaller.domain.CabinGroupUser;

import com.mastertek.cabcaller.repository.CabinGroupUserRepository;
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
 * REST controller for managing CabinGroupUser.
 */
@RestController
@RequestMapping("/api")
public class CabinGroupUserResource {

    private final Logger log = LoggerFactory.getLogger(CabinGroupUserResource.class);

    private static final String ENTITY_NAME = "cabinGroupUser";

    private final CabinGroupUserRepository cabinGroupUserRepository;

    public CabinGroupUserResource(CabinGroupUserRepository cabinGroupUserRepository) {
        this.cabinGroupUserRepository = cabinGroupUserRepository;
    }

    /**
     * POST  /cabin-group-users : Create a new cabinGroupUser.
     *
     * @param cabinGroupUser the cabinGroupUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabinGroupUser, or with status 400 (Bad Request) if the cabinGroupUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabin-group-users")
    @Timed
    public ResponseEntity<CabinGroupUser> createCabinGroupUser(@Valid @RequestBody CabinGroupUser cabinGroupUser) throws URISyntaxException {
        log.debug("REST request to save CabinGroupUser : {}", cabinGroupUser);
        if (cabinGroupUser.getId() != null) {
            throw new BadRequestAlertException("A new cabinGroupUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CabinGroupUser result = cabinGroupUserRepository.save(cabinGroupUser);
        return ResponseEntity.created(new URI("/api/cabin-group-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabin-group-users : Updates an existing cabinGroupUser.
     *
     * @param cabinGroupUser the cabinGroupUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabinGroupUser,
     * or with status 400 (Bad Request) if the cabinGroupUser is not valid,
     * or with status 500 (Internal Server Error) if the cabinGroupUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabin-group-users")
    @Timed
    public ResponseEntity<CabinGroupUser> updateCabinGroupUser(@Valid @RequestBody CabinGroupUser cabinGroupUser) throws URISyntaxException {
        log.debug("REST request to update CabinGroupUser : {}", cabinGroupUser);
        if (cabinGroupUser.getId() == null) {
            return createCabinGroupUser(cabinGroupUser);
        }
        CabinGroupUser result = cabinGroupUserRepository.save(cabinGroupUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabinGroupUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabin-group-users : get all the cabinGroupUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cabinGroupUsers in body
     */
    @GetMapping("/cabin-group-users")
    @Timed
    public List<CabinGroupUser> getAllCabinGroupUsers() {
        log.debug("REST request to get all CabinGroupUsers");
        return cabinGroupUserRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /cabin-group-users/:id : get the "id" cabinGroupUser.
     *
     * @param id the id of the cabinGroupUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabinGroupUser, or with status 404 (Not Found)
     */
    @GetMapping("/cabin-group-users/{id}")
    @Timed
    public ResponseEntity<CabinGroupUser> getCabinGroupUser(@PathVariable Long id) {
        log.debug("REST request to get CabinGroupUser : {}", id);
        CabinGroupUser cabinGroupUser = cabinGroupUserRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cabinGroupUser));
    }

    /**
     * DELETE  /cabin-group-users/:id : delete the "id" cabinGroupUser.
     *
     * @param id the id of the cabinGroupUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabin-group-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabinGroupUser(@PathVariable Long id) {
        log.debug("REST request to delete CabinGroupUser : {}", id);
        cabinGroupUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
