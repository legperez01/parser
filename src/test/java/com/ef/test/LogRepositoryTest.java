package com.ef.test;

import com.ef.repository.LogRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestContextManager;

@RunWith(Parameterized.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackages = "com.ef")
public class LogRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    LogRepository logRepository;
    private TestContextManager testContextManager;

    @Parameterized.Parameter(value = 0)
    public LocalDateTime startDate;

    @Parameterized.Parameter(value = 1)
    public LocalDateTime maxDate;

    @Parameterized.Parameter(value = 2)
    public Long threshold;

    @Parameterized.Parameter(value = 3)
    public List<String> expectedList;

    @Parameterized.Parameters
    public static List<Object> data() {
        return Arrays.asList(new Object[]{LocalDateTime.parse("2017-01-01.15:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            LocalDateTime.parse("2017-01-01.16:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            Long.valueOf(200),
                            Arrays.asList("192.168.106.134", "192.168.11.231")},
                            new Object[]{LocalDateTime.parse("2017-01-01.09:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            LocalDateTime.parse("2017-01-01.10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            Long.valueOf(200),
                            Arrays.asList("192.168.33.154", "192.168.57.74", "192.168.73.195", "192.168.85.118", "192.168.90.115")},
                            new Object[]{LocalDateTime.parse("2017-01-01.13:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            LocalDateTime.parse("2017-01-02.13:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            Long.valueOf(300),
                            Arrays.asList("192.168.129.191", "192.168.143.177", "192.168.199.209", "192.168.203.111", "192.168.38.77")},
                            new Object[]{LocalDateTime.parse("2017-01-01.13:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            LocalDateTime.parse("2017-01-02.13:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")),
                            Long.valueOf(500),
                            Arrays.asList()});

    }

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Test
    public void getIpListOverMaximunIpRequestBetweenStartDateAndMaxDateTest() {
        assertThat(this.logRepository.getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(this.startDate, 
                                                                                            this.maxDate, 
                                                                                            this.threshold))
                                                                                            .isEqualTo(this.expectedList);
    }

}
