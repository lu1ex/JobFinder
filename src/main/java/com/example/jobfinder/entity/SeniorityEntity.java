package com.example.jobfinder.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Seniority")
@AllArgsConstructor
@NoArgsConstructor
public class SeniorityEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String name;
    @ManyToMany(mappedBy = "seniority")
    private Set<UnifiedOfferEntity> unifiedOffers = new HashSet<>();
    @ManyToMany(mappedBy = "ownedSeniority")
    private Set<UserPreferencesEntity> usersPreferences = new HashSet<>();

    public SeniorityEntity(String  name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.unifiedOffers = new HashSet<>();
    }

    @Override
    public String toString() {
        return "SeniorityEntity{" +
                ", seniority='" + name + '\'' +
                '}';
    }
}
