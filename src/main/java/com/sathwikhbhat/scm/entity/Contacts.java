package com.sathwikhbhat.scm.entity;

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
public class Contacts {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String pictureUrl;
    private String description;
    private boolean favourite;
    private String websiteLink;
    private String linkedinLink;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    private List<SocialLinks> links = new ArrayList<>();

}