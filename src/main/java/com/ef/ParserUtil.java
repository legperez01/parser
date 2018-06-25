/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author lgonzalez
 */
public class ParserUtil {
    
    public static List<Log> parse(String urlFile){
    	
    	try {
			List<Log> logs = Files.lines(Paths.get(urlFile)).map(line->line.split("\\|"))
			.map(arr->{
		
				LocalDateTime date = LocalDateTime.parse(arr[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnn"));
				String ip = arr[1];
				String request = arr[2];
				int status = Integer.parseInt(arr[3]);
				String userAgent = arr[4];
				return new Log(date, ip, request, status, userAgent);
			}).collect(Collectors.toList());
			
			return logs;
		} catch (IOException e) {						
			e.printStackTrace();
			return null;		
		}
                  
    } 
    
    public static void printList(List<Log> list){
    	
    	list.stream().map(log->log.getIp()).forEach(System.out::println);
    }
    
}
