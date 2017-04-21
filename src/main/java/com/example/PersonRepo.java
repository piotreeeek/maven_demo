package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Piotrek on 2017-04-15.
 */
public interface PersonRepo extends JpaRepository<Person, Long> {
}