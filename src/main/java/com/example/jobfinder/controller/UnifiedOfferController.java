package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.modelDTO.SkillDTO;
import com.example.jobfinder.modelDTO.UnifiedOfferDTO;
import com.example.jobfinder.requestBodyModels.SkillRequestBodyModel;
import com.example.jobfinder.requestBodyModels.UnifiedOfferRequestBodyModel;
import com.example.jobfinder.services.MapperService;
import com.example.jobfinder.services.UnifiedOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unifiedOffer")
public class UnifiedOfferController {
    private final MapperService mapperService;
    private final UnifiedOfferService unifiedOfferService;

    @PostMapping
    public ResponseEntity<UnifiedOfferDTO> createUnifiedOffer(@RequestBody UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
        UnifiedOfferEntity unifiedOfferEntity = unifiedOfferService.createUnifiedOfferEntity(unifiedOfferRequestBodyModel);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(unifiedOfferEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnifiedOfferDTO> getUnifiedOffer(@PathVariable String id) {
        UnifiedOfferEntity unifiedOfferEntity = unifiedOfferService.getUnifiedOfferEntityById(id);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(unifiedOfferEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
       /* List<SkillEntity> skillEntityList = skillService.getAllSkillEntities();
        List<SkillDTO> skillDTOList = skillEntityList.stream()
                .map(mapperService::mapSkillEntityToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(skillDTOList, HttpStatus.OK);
        */
        return null;

    }

    /*@PutMapping("/{id}")
    public ResponseEntity<SkillDTO> updatedSkillByPut(@PathVariable String id, @RequestBody SkillRequestBodyModel skillRequestBodyModel)  {
        SkillEntity skillEntity = skillService.updateSkillEntityByPut(id, skillRequestBodyModel);
        SkillDTO skillDTO = mapperService.mapSkillEntityToDTO(skillEntity);
        return new ResponseEntity<>(skillDTO, HttpStatus.OK);
    }*/

    @PutMapping("/{id}")
    public String updatedSkillByPut(@PathVariable String id, @RequestBody SkillRequestBodyModel skillRequestBodyModel)  {
       /* SkillEntity skillEntity = skillService.updateSkillEntityByPut(id, skillRequestBodyModel);
        // SkillDTO skillDTO = mapperService.mapSkillEntityToDTO(skillEntity);
        Set<UnifiedOfferEntity> list = skillEntity.getUnifiedOffers();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(skillEntity.getName());
        for (UnifiedOfferEntity unifiedOfferEntity : list) {
            stringBuilder.append(unifiedOfferEntity.getCompanyName() + " ");
        }
        return stringBuilder.toString();*/
        return null;
    }

    @PatchMapping
    public String updatedSkillByPatch() {

        return "Not implemented yet";
    }

    @DeleteMapping("/id")
    public ResponseEntity deleteSkill(@PathVariable String id) {
       /* skillService.deleteSkillEntityByID(id);
        return new ResponseEntity(HttpStatus.OK);
        */
        return null;
    }

}
