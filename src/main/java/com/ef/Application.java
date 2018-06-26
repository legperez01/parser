package com.ef;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    LogService logService;    
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
                               
                this.logService.proccess(accessLog, startDate, duration, threshold);

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
        } catch(IOException e){
            logger.error(e.getMessage(), e);
        }
            
    }   
    
}
