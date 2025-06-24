package com.sathwikhbhat.scm.entity;

import com.sathwikhbhat.scm.enums.Providers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String phoneNumber;
    private String about;
    private String profilePictureUrl;

    private boolean enabled;
    private boolean emailVerified;
    private boolean phoneVerified;

    private Providers provider;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contacts> contacts = new ArrayList<>();

}