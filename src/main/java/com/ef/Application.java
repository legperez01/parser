package com.ef;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    LogRepository logRepository;
    @Autowired
    BlockIpRepository blockIpRepository;
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String accessLog;
        LocalDateTime startDate;
        Duration duration;
        Long threshold;

        try {
            logger.info(String.valueOf(args.getOptionNames().size()));
            if (args.getOptionNames().size() > 0 && args.getOptionNames().size() == 4) {
                accessLog = args.getOptionValues("accesslog").get(0);
                startDate = LocalDateTime.parse(args.getOptionValues("startDate").get(0), 
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
                duration = Duration.valueOf(args.getOptionValues("duration").get(0).toUpperCase());
                threshold = Long.valueOf(args.getOptionValues("threshold").get(0));
                
                logger.info(accessLog);
                logger.info(startDate.toString());
                logger.info(duration.toString());
                logger.info(threshold.toString());
            /**   
                List<Log> list = ParserUtil.parse(accessLog);
                    list.stream().forEach(log -> {
                        try {
                            this.logRepository.save(log);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    });
*/
                this.proccess(startDate, duration, threshold);

            }else{
                logger.error("The following parameters are require: --accesslog='path to the log file'"
                    + " --startDate='valid date'"
                    + " --duration='hourly or daily'"
                    + " --threshold='valid integer'");
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error(e.getMessage());
            logger.error("Valid parameters: --accesslog='path to the log file'"
                    + " --startDate='valid date'"
                    + " --duration='hourly or daily'"
                    + " --threshold='valid integer'");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            logger.error("Valid parameters: --accesslog='path to the log file' "
                    + "--startDate='valid date'"
                    + " --duration='hourly or daily' "
                    + "--threshold='valid integer'");
        } //catch(IOException e){
//            logger.error(e.getMessage(), e);
//        }
            
    }

    public void proccess(LocalDateTime startDate, Duration duration, Long threshold) {
        
        LocalDateTime maxDate = duration.equals(Duration.HOURLY) ? startDate.plusHours(1) : startDate.plusDays(1);
        logger.info(startDate + " - " + maxDate);
        List<String> ipList = this.logRepository.getIpListOverMaximunIpRequestBetweenStartDateAndMaxDate(startDate, maxDate, threshold);
        ipList.stream().forEach(ip -> logger.info(ip));
        ipList.stream().forEach(ip -> this.blockIpRepository.save(new BlockIp(ip, "Block because has more than " + threshold + " request")));
    }

}
