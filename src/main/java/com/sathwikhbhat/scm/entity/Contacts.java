package com.sathwikhbhat.scm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isFavorite = false;
    private String websiteLink;
    private String linkedinLink;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLinks> links = new ArrayList<>();

}