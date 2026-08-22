package com.example.springparent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParentApplication {
  private static Logger logger = LoggerFactory.getLogger(ParentApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ParentApplication.class, args);
    logger.info("Parent application started");
  }
}
