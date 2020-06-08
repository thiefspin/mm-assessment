package com.thiefspin.javasolution.repository;

import com.thiefspin.javasolution.entity.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findBySessionIdAndCompleted(String sessionId, boolean completed);

}
