package com.promptengineer.dreamsoccer.repository;
/*
IntelliJ IDEA 2024.3.2.2 (Community Edition)
Build #IC-243.23654.189, built on January 29, 2025
@Author TGS a.k.a. Dwitio Ahmad Pranoto
Java Developer
Created on 11/02/2025 13:47
@Last Modified 11/02/2025 13:47
Version 1.0
*/

import com.promptengineer.dreamsoccer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
