package com.sonbook.song.repository;

import com.sonbook.song.domain.Chant;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Chant entity.
 */
public interface ChantRepository extends MongoRepository<Chant,String> {

}
