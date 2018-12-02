package com.mastertek.cabcaller.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.cabcaller.domain.Cabin;

import com.mastertek.cabcaller.repository.CabinRepository;
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
 * REST controller for managing Cabin.
 */
@RestController
@RequestMapping("/api")
public class CabinResource {

    private final Logger log = LoggerFactory.getLogger(CabinResource.class);

    private static final String ENTITY_NAME = "cabin";

    private final CabinRepository cabinRepository;

    public CabinResource(CabinRepository cabinRepository) {
        this.cabinRepository = cabinRepository;
    }

    /**
     * POST  /cabins : Create a new cabin.
     *
     * @param cabin the cabin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabin, or with status 400 (Bad Request) if the cabin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabins")
    @Timed
    public ResponseEntity<Cabin> createCabin(@Valid @RequestBody Cabin cabin) throws URISyntaxException {
        log.debug("REST request to save Cabin : {}", cabin);
        if (cabin.getId() != null) {
            throw new BadRequestAlertException("A new cabin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cabin result = cabinRepository.save(cabin);
        return ResponseEntity.created(new URI("/api/cabins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabins : Updates an existing cabin.
     *
     * @param cabin the cabin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabin,
     * or with status 400 (Bad Request) if the cabin is not valid,
     * or with status 500 (Internal Server Error) if the cabin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabins")
    @Timed
    public ResponseEntity<Cabin> updateCabin(@Valid @RequestBody Cabin cabin) throws URISyntaxException {
        log.debug("REST request to update Cabin : {}", cabin);
        if (cabin.getId() == null) {
            return createCabin(cabin);
        }
        Cabin result = cabinRepository.save(cabin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabins : get all the cabins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cabins in body
     */
    @GetMapping("/cabins")
    @Timed
    public List<Cabin> getAllCabins() {
        log.debug("REST request to get all Cabins");
        return cabinRepository.findAll();
        }

    /**
     * GET  /cabins/:id : get the "id" cabin.
     *
     * @param id the id of the cabin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabin, or with status 404 (Not Found)
     */
    @GetMapping("/cabins/{id}")
    @Timed
    public ResponseEntity<Cabin> getCabin(@PathVariable Long id) {
        log.debug("REST request to get Cabin : {}", id);
        Cabin cabin = cabinRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cabin));
    }

    /**
     * DELETE  /cabins/:id : delete the "id" cabin.
     *
     * @param id the id of the cabin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabins/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabin(@PathVariable Long id) {
        log.debug("REST request to delete Cabin : {}", id);
        cabinRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
