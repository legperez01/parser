package com.example.demo;

import com.ef.LogRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class LogRepositoryTest {
    
    @Autowired
    LogRepository logRepository;

	@Test
	public void getIpListOverMaximunIpRequestBetweenStartDateAndMaxDateTest() {
            
            List<String> ipList = Arrays.asList("192.168.106.134", "192.168.11.231");
            
            assertThat(ipList).isEqualTo(logRepository.getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(
                            LocalDateTime.parse("2017-01-01 12:00:00"),
                            LocalDateTime.parse("2017-01-01 13:00:00"),
                            Long.valueOf(200)));
                    
	}
        
        

}
