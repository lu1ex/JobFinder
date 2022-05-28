package com.example.jobfinder.modelDTO;

import com.example.jobfinder.enums.TypeOfContract;
import lombok.Data;


@Data
public class ContractDetailsDTO {
    private TypeOfContract typeOfContract;
    private double salaryFrom;
    private double salaryTo;
}
