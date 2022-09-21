package com.example.securitystart;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    List<Admin> findByUsername(String name);
}
