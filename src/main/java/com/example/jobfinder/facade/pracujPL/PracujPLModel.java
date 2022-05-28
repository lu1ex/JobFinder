package com.example.jobfinder.facade.pracujPL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Data
@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class PracujPLModel {
    private int offerId;
    private String offerUrl;//
    private String location;//street zawsze null bo nie ma
    private String jobTitle;//
    private String employer;//
    private LocalDate lastPublicated;//
    private LocalDate expirationDate;
    private String salary;//
    private String employmentLevel;//
    private List<String> typesOfContract;
    private boolean remoteWork;//
    private boolean isRemoteRecruitment;//
    private List<Object> technologiesExpected;//


}
