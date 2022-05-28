package com.example.jobfinder.entity;

import com.example.jobfinder.requestBodyModels.SkillRequestBodyModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Skills")
@AllArgsConstructor
@NoArgsConstructor
public class SkillEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    @ManyToMany(mappedBy = "skills")
    private Set<UnifiedOfferEntity> unifiedOffers = new HashSet<>();

    public SkillEntity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public SkillEntity (SkillRequestBodyModel skillRequestBodyModel) {
        this.id = UUID.randomUUID().toString();
        this.name = skillRequestBodyModel.getName();
        this.unifiedOffers = new HashSet<>();
    }
}
