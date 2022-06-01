package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.modelDTO.UnifiedOfferDTO;
import com.example.jobfinder.requestBodyModels.UnifiedOfferRequestBodyModel;
import com.example.jobfinder.services.MapperService;
import com.example.jobfinder.services.UnifiedOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unifiedOffer")
public class UnifiedOfferController {
    private final MapperService mapperService;
    private final UnifiedOfferService unifiedOfferService;

    @PostMapping
    public ResponseEntity<UnifiedOfferDTO> createUnifiedOffer(@RequestBody UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
        UnifiedOfferEntity unifiedOfferEntity = unifiedOfferService.createEntity(unifiedOfferRequestBodyModel);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(unifiedOfferEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnifiedOfferDTO> getUnifiedOffer(@PathVariable String id) {
        UnifiedOfferEntity unifiedOfferEntity = unifiedOfferService.getEntityById(id);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(unifiedOfferEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<UnifiedOfferDTO>> getAllUnifiedOffers() {
        Set<UnifiedOfferEntity> unifiedOfferEntityList = unifiedOfferService.getAllEntities();
        Set<UnifiedOfferDTO> unifiedOfferDTOList = mapperService.mapSetToSetOfClass(unifiedOfferEntityList, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUnifiedOffer(@PathVariable String id) {
        unifiedOfferService.deleteEntityById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnifiedOfferDTO> updateEntityByPut(
            @PathVariable String id, @RequestBody UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
        UnifiedOfferEntity updatedEntity = unifiedOfferService.putEntity(id, unifiedOfferRequestBodyModel);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(updatedEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UnifiedOfferDTO> updateEntityByPatch(
            @PathVariable String id, @RequestBody Map<Object, Object> fields) {
        UnifiedOfferEntity updatedEntity = unifiedOfferService.patchEntity(id, fields);
        UnifiedOfferDTO unifiedOfferDTO = mapperService.mapObjectToOtherObject(updatedEntity, UnifiedOfferDTO.class);
        return new ResponseEntity<>(unifiedOfferDTO, HttpStatus.OK);
    }
}
