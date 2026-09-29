package com.example.SchoolWebsite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.SchoolWebsite.repository.StudentRepository;

// A CommandLineRunner's run() method executes once, right after startup.
// TEMPORARY: used only to prove the database query works.
@Component
public class TempRunner implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public TempRunner(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("---- Students from MySQL ----");
        studentRepository.findAll().forEach(System.out::println);
        System.out.println("-----------------------------");
    }
}
