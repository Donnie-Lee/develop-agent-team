package com.interviewai.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.interviewai.user",
    "com.interviewai.resume",
    "com.interviewai.interview",
    "com.interviewai.question",
    "com.interviewai.notify",
    "com.interviewai.app"
})
public class InterviewAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewAIApplication.class, args);
    }
}
