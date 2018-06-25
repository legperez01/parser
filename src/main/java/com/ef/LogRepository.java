/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author lgonzalez
 */
public interface LogRepository extends CrudRepository<Log, Long>{
	
        @Query("SELECT l.ip FROM Log l WHERE date BETWEEN :startDate AND :maxDate GROUP BY l.ip HAVING COUNT(l.ip) >= :threshold")
        List<String> getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(@Param("startDate") LocalDateTime startDate,
                                                                             @Param("maxDate") LocalDateTime maxtDate,
                                                                             @Param("threshold") Long threshold);
    
}
