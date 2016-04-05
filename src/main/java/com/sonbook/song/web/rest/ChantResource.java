package com.sonbook.song.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sonbook.song.domain.Chant;
import com.sonbook.song.repository.ChantRepository;
import com.sonbook.song.web.rest.util.HeaderUtil;
import com.sonbook.song.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Chant.
 */
@RestController
@RequestMapping("/api")
public class ChantResource {

    private final Logger log = LoggerFactory.getLogger(ChantResource.class);

    @Inject
    private ChantRepository chantRepository;

    /**
     * POST  /chants -> Create a new chant.
     */
    @RequestMapping(value = "/chants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chant> createChant(@RequestBody Chant chant) throws URISyntaxException {
        log.debug("REST request to save Chant : {}", chant);
        if (chant.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new chant cannot already have an ID").body(null);
        }
        Chant result = chantRepository.save(chant);
        return ResponseEntity.created(new URI("/api/chants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chants -> Updates an existing chant.
     */
    @RequestMapping(value = "/chants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chant> updateChant(@RequestBody Chant chant) throws URISyntaxException {
        log.debug("REST request to update Chant : {}", chant);
        if (chant.getId() == null) {
            return createChant(chant);
        }
        Chant result = chantRepository.save(chant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chant", chant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chants -> get all the chants.
     */
    @RequestMapping(value = "/chants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Chant>> getAllChants(Pageable pageable)
        throws URISyntaxException {
        Page<Chant> page = chantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chants/:id -> get the "id" chant.
     */
    @RequestMapping(value = "/chants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chant> getChant(@PathVariable String id) {
        log.debug("REST request to get Chant : {}", id);
        return Optional.ofNullable(chantRepository.findOne(id))
            .map(chant -> new ResponseEntity<>(
                chant,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chants/:id -> delete the "id" chant.
     */
    @RequestMapping(value = "/chants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChant(@PathVariable String id) {
        log.debug("REST request to delete Chant : {}", id);
        chantRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chant", id.toString())).build();
    }
}
