package com.market.transactionguard.component;

import com.market.transactionguard.entities.Role;
import com.market.transactionguard.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class RoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findByAuthority("BUYER") == null){
            Role userRole = new Role("BUYER");
            roleRepository.save(userRole);
        }

        if(roleRepository.findByAuthority("SELLER") == null){
            Role userRole = new Role("SELLER");
            roleRepository.save(userRole);
        }
    }
}
