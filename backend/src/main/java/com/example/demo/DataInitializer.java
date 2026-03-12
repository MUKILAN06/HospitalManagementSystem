package com.example.demo;

import com.example.demo.model.AvailableSlot;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByRole(Role.DOCTOR).isEmpty()) {
            List<User> doctors = new ArrayList<>();

            doctors.add(createDoctor("Dr. Smith", "smith@hospital.com", "Cardiology"));
            doctors.add(createDoctor("Dr. Johnson", "johnson@hospital.com", "Neurology"));
            doctors.add(createDoctor("Dr. Williams", "williams@hospital.com", "Pediatrics"));
            doctors.add(createDoctor("Dr. Brown", "brown@hospital.com", "Orthopedics"));
            doctors.add(createDoctor("Dr. Jones", "jones@hospital.com", "Dermatology"));
            doctors.add(createDoctor("Dr. Garcia", "garcia@hospital.com", "Oncology"));
            doctors.add(createDoctor("Dr. Miller", "miller@hospital.com", "Gastroenterology"));
            doctors.add(createDoctor("Dr. Davis", "davis@hospital.com", "Psychiatry"));
            doctors.add(createDoctor("Dr. Rodriguez", "rodriguez@hospital.com", "Ophthalmology"));
            doctors.add(createDoctor("Dr. Martinez", "martinez@hospital.com", "Urology"));

            userRepository.saveAll(doctors);
            System.out.println("Default doctors initialized.");
        }
    }

    private User createDoctor(String name, String email, String specialization) {
        User doctor = new User();
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPassword("doctor123"); // Default password
        doctor.setRole(Role.DOCTOR);
        doctor.setSpecialization(specialization);

        // Add some default slots for today and tomorrow
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<AvailableSlot> slots = Arrays.asList(
            new AvailableSlot(today, LocalTime.of(9, 0), LocalTime.of(10, 0)),
            new AvailableSlot(today, LocalTime.of(10, 0), LocalTime.of(11, 0)),
            new AvailableSlot(today, LocalTime.of(14, 0), LocalTime.of(15, 0)),
            new AvailableSlot(tomorrow, LocalTime.of(9, 0), LocalTime.of(10, 0)),
            new AvailableSlot(tomorrow, LocalTime.of(11, 0), LocalTime.of(12, 0))
        );

        doctor.setAvailableSlots(new ArrayList<>(slots));
        return doctor;
    }
}
