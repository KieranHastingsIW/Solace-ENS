package com.restsolace.solacerest.Entity;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Gp_Email_Table")
public class GpEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NonNull
    @Column(name = "GP_name", nullable = false)
    private String gp_name;
    
    @NonNull
    @Column(name = "Gp_email", nullable = false)
    private String gp_email;

}