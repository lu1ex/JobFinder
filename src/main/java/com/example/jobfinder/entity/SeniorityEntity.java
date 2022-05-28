package com.example.jobfinder.entity;

import com.example.jobfinder.enums.Seniority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "Seniority")
@AllArgsConstructor
@NoArgsConstructor
public class SeniorityEntity {
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @Enumerated(EnumType.STRING)
    private Seniority seniority;
    @ManyToMany(mappedBy = "seniority")
    private Set<UnifiedOfferEntity> unifiedOffers = new HashSet<>();

    public SeniorityEntity(Seniority seniority) {
        this.id = UUID.randomUUID().toString();
        this.seniority = seniority;
        this.unifiedOffers = new HashSet<>();
    }


}
