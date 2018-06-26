/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    public void proccess(String accessLog, LocalDateTime startDate, Duration duration, Long threshold) throws IOException {
        
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
        logger.info("List of block IPs");
        ipList.stream().forEach(ip -> logger.info(ip));
        ipList.stream().forEach(ip -> this.blockIpRepository.save(new BlockIp(ip, "Block because has more than " + threshold + " request")));
    }
    
}
