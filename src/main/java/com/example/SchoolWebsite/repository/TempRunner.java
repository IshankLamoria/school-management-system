// FIX: Package declaration was 'com.example.SchoolWebsite' (the root package) but this file
// physically lives under the 'repository/' sub-folder. Java resolves classes by their declared
// package, so javac compiled it fine, but IDEs flagged it with a 'package does not match
// directory structure' warning (red/yellow squiggle on line 1).
// Corrected to match the actual folder path: com.example.SchoolWebsite.repository
package com.example.SchoolWebsite.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// import com.example.SchoolWebsite.repository.StudentRepository;

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
