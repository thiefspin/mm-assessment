package com.thiefspin.javasolution.repository;

import com.thiefspin.javasolution.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Long> {


}
