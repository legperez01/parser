/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef.service;

import com.ef.Parser;
import com.ef.entity.BlockIp;
import com.ef.repository.BlockIpRepository;
import com.ef.Duration;
import com.ef.entity.Log;
import com.ef.repository.LogRepository;
import com.ef.ParserUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lgonzalez
 */
@Service
public class LogService {
    
    @Autowired
    LogRepository logRepository;
    @Autowired
    BlockIpRepository blockIpRepository;   
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);
    
    public void proccess(String accessLog, LocalDateTime startDate, Duration duration, Long threshold) throws IOException  {
        
        try {
            List<Log> list = ParserUtil.parse(accessLog);
            list.stream().forEach(log -> {
                try {
                    this.logRepository.save(log);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
            
            LocalDateTime maxDate = duration.equals(Duration.HOURLY) ? startDate.plusHours(1) : startDate.plusDays(1);
            logger.info(startDate + " - " + maxDate);
            List<String> ipList = this.logRepository.getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(startDate, maxDate, threshold);
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
            logger.info("List of block IPs over " + threshold + " request between " + startDate.format(f) + " and " + maxDate.format(f));
            ipList.stream().forEach(ip -> logger.info(ip));
            ipList.stream().forEach(ip -> this.blockIpRepository.save(new BlockIp(ip, "Block because has more than " + threshold + " request")));
        
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } 
        
    }
    
}
