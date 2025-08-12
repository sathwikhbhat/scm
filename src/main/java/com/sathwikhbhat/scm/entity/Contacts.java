package com.sathwikhbhat.scm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Builder.Default
    private boolean favourite = false;
    private String websiteLink;
    private String linkedinLink;

    @ManyToOne
    @JsonIgnore
    private User user;

}