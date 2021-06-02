package de.hs.bochum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.hs.bochum.entities.messung.Messreihe;

@Repository
public interface MessungsRepository extends JpaRepository<Messreihe, Integer> {
}
