package com.example.demo.config;

import com.example.demo.model.AvailableSlot;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@hospital.com")) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@hospital.com");
                admin.setPassword("admin123");
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Default admin created: admin@hospital.com / admin123");
            }

            if (userRepository.findByRole(Role.DOCTOR).isEmpty()) {
                List<User> newDoctors = Arrays.asList(
                        createDoctor("Dr. Smith", "smith@hospital.com", "Cardiology"),
                        createDoctor("Dr. Johnson", "johnson@hospital.com", "Neurology"),
                        createDoctor("Dr. Williams", "williams@hospital.com", "Pediatrics"),
                        createDoctor("Dr. Brown", "brown@hospital.com", "Orthopedics"),
                        createDoctor("Dr. Jones", "jones@hospital.com", "Dermatology"),
                        createDoctor("Dr. Garcia", "garcia@hospital.com", "Oncology"),
                        createDoctor("Dr. Miller", "miller@hospital.com", "Gastroenterology"),
                        createDoctor("Dr. Davis", "davis@hospital.com", "Psychiatry"),
                        createDoctor("Dr. Rodriguez", "rodriguez@hospital.com", "Ophthalmology"),
                        createDoctor("Dr. Martinez", "martinez@hospital.com", "Urology")
                );
                userRepository.saveAll(newDoctors);
                System.out.println("10 Default doctors initialized.");
            }

            // Seed default slots for all doctors if they have no slots
            List<User> doctors = userRepository.findByRole(Role.DOCTOR);
            for (User doctor : doctors) {
                if (doctor.getAvailableSlots() == null || doctor.getAvailableSlots().isEmpty()) {
                    System.out.println("Adding default slots for doctor: " + doctor.getName());
                    if (doctor.getAvailableSlots() == null) {
                        doctor.setAvailableSlots(new ArrayList<>());
                    }
                    LocalDate today = LocalDate.now();
                    LocalDate tomorrow = today.plusDays(1);
                    doctor.getAvailableSlots().add(new AvailableSlot(today, LocalTime.of(9, 0), LocalTime.of(10, 0)));
                    doctor.getAvailableSlots().add(new AvailableSlot(today, LocalTime.of(10, 0), LocalTime.of(11, 0)));
                    doctor.getAvailableSlots().add(new AvailableSlot(today, LocalTime.of(14, 0), LocalTime.of(15, 0)));
                    doctor.getAvailableSlots().add(new AvailableSlot(tomorrow, LocalTime.of(9, 0), LocalTime.of(10, 0)));
                    doctor.getAvailableSlots().add(new AvailableSlot(tomorrow, LocalTime.of(11, 0), LocalTime.of(12, 0)));
                    userRepository.save(doctor);
                }
            }
        };
    }

    private User createDoctor(String name, String email, String specialization) {
        User doctor = new User();
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPassword("doctor123");
        doctor.setRole(Role.DOCTOR);
        doctor.setSpecialization(specialization);
        doctor.setAvailableSlots(new ArrayList<>());
        return doctor;
    }
}
