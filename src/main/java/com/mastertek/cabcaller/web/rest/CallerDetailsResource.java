package com.mastertek.cabcaller.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.cabcaller.domain.CallerDetails;

import com.mastertek.cabcaller.repository.CallerDetailsRepository;
import com.mastertek.cabcaller.web.rest.errors.BadRequestAlertException;
import com.mastertek.cabcaller.web.rest.util.HeaderUtil;
import com.mastertek.cabcaller.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CallerDetails.
 */
@RestController
@RequestMapping("/api")
public class CallerDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CallerDetailsResource.class);

    private static final String ENTITY_NAME = "callerDetails";

    private final CallerDetailsRepository callerDetailsRepository;

    public CallerDetailsResource(CallerDetailsRepository callerDetailsRepository) {
        this.callerDetailsRepository = callerDetailsRepository;
    }

    /**
     * POST  /caller-details : Create a new callerDetails.
     *
     * @param callerDetails the callerDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new callerDetails, or with status 400 (Bad Request) if the callerDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/caller-details")
    @Timed
    public ResponseEntity<CallerDetails> createCallerDetails(@RequestBody CallerDetails callerDetails) throws URISyntaxException {
        log.debug("REST request to save CallerDetails : {}", callerDetails);
        if (callerDetails.getId() != null) {
            throw new BadRequestAlertException("A new callerDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CallerDetails result = callerDetailsRepository.save(callerDetails);
        return ResponseEntity.created(new URI("/api/caller-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /caller-details : Updates an existing callerDetails.
     *
     * @param callerDetails the callerDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated callerDetails,
     * or with status 400 (Bad Request) if the callerDetails is not valid,
     * or with status 500 (Internal Server Error) if the callerDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/caller-details")
    @Timed
    public ResponseEntity<CallerDetails> updateCallerDetails(@RequestBody CallerDetails callerDetails) throws URISyntaxException {
        log.debug("REST request to update CallerDetails : {}", callerDetails);
        if (callerDetails.getId() == null) {
            return createCallerDetails(callerDetails);
        }
        CallerDetails result = callerDetailsRepository.save(callerDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, callerDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /caller-details : get all the callerDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of callerDetails in body
     */
    @GetMapping("/caller-details")
    @Timed
    public ResponseEntity<List<CallerDetails>> getAllCallerDetails(Pageable pageable) {
        log.debug("REST request to get a page of CallerDetails");
        Page<CallerDetails> page = callerDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/caller-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /caller-details/:id : get the "id" callerDetails.
     *
     * @param id the id of the callerDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the callerDetails, or with status 404 (Not Found)
     */
    @GetMapping("/caller-details/{id}")
    @Timed
    public ResponseEntity<CallerDetails> getCallerDetails(@PathVariable Long id) {
        log.debug("REST request to get CallerDetails : {}", id);
        CallerDetails callerDetails = callerDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(callerDetails));
    }

    /**
     * DELETE  /caller-details/:id : delete the "id" callerDetails.
     *
     * @param id the id of the callerDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/caller-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCallerDetails(@PathVariable Long id) {
        log.debug("REST request to delete CallerDetails : {}", id);
        callerDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
