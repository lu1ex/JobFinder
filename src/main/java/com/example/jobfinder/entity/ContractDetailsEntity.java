package com.example.jobfinder.entity;

import com.example.jobfinder.enums.TypeOfContract;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "ContractDetails")
@AllArgsConstructor
@NoArgsConstructor
public class ContractDetailsEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Enumerated(EnumType.STRING)
    private TypeOfContract typeOfContract;
    private double salaryFrom;
    private double salaryTo;
    @ManyToMany(mappedBy = "contractDetails")
    private Set<UnifiedOfferEntity> unifiedOffers = new HashSet<>();


    public ContractDetailsEntity(TypeOfContract typeOfContract, double salaryFrom, double salaryTo) {
        this.id = UUID.randomUUID().toString();
        this.typeOfContract = typeOfContract;
        this.salaryFrom = salaryFrom;
        this.salaryTo = salaryTo;
    }

    @Override
    public String toString() {
        return "ContractDetailsEntity{" +
                ", typeOfContract=" + typeOfContract +
                ", salaryFrom=" + salaryFrom +
                ", salaryTo=" + salaryTo +
                '}';
    }
}
