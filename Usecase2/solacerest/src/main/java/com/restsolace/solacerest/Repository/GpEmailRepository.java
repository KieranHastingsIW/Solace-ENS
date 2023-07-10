package com.restsolace.solacerest.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restsolace.solacerest.Entity.GpEmail;
import java.util.List;







public interface GpEmailRepository extends JpaRepository<GpEmail, Long>{
    List<GpEmail> findByGp_name(String gp_name);
}
