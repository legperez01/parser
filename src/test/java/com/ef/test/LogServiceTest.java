/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef.test;

import com.ef.repository.LogRepository;
import com.ef.service.LogService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author lgonzalez
 */
@RunWith(SpringRunner.class)
public class LogServiceTest {
    
    @TestConfiguration
    static class LogServiceTestConfiguration {

        @Bean
        public LogService employeeService() {
            return new LogService();
        }
    }
    
    @Autowired
    public LogService logService;
    
    @MockBean
    LogRepository logRepository;
    
    @Before
    public void setUp(){
        List<String> ipList = Arrays.asList("192.168.106.134", "192.168.11.231");
        
        Mockito.when(this.logRepository.getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(LocalDateTime.now(), LocalDateTime.now(), new Long(0))).thenReturn(ipList);
    }
    
    
}
