package com.lcwd.store;

import com.lcwd.store.entities.Role;
import com.lcwd.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EmptyStackException;
import java.util.UUID;


@SpringBootApplication
public class ElectroStoreApplication implements CommandLineRunner {

    @Value("${admin.role.id}")
    private String role_admin_id;
    @Value("${normal.role.id}")
    private String role_normal_id;
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {

        SpringApplication.run(ElectroStoreApplication.class, args);
    }

    public void run(String... args) throws Exception {
        try {

            Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
            Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
            roleRepository.save(role_normal);
            roleRepository.save(role_admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
