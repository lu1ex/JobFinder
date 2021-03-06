package com.example.jobfinder.services;

import com.example.jobfinder.entity.*;
import com.example.jobfinder.exceptions.ValueNotFoundException;
import com.example.jobfinder.forms.RegisterForm;
import com.example.jobfinder.forms.UserPreferencesForm;
import com.example.jobfinder.repository.SeniorityEntityRepository;
import com.example.jobfinder.repository.SkillEntityRepository;
import com.example.jobfinder.repository.UserEntityRepository;
import com.example.jobfinder.requestBodyModels.UserRequestBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final SkillEntityRepository skillEntityRepository;
    private final SeniorityEntityRepository seniorityEntityRepository;
    private final MapperService mapperService;
    private final HelperService helperService;

    public void activateUserAccount(String id) {
        UserEntity userEntity = getEntityById(id);
        userEntity.setActiveAccount(true);
        userEntityRepository.saveAndFlush(userEntity);
    }

    public void saveUserPreferences(UserPreferencesForm userPreferencesForm) {
        UserEntity userEntity = helperService.getCurrentLoggedUser();
        Set<SeniorityEntity> ownedSeniority =
                userPreferencesForm.getOwnedSeniority().stream()
                                .map(seniorityName -> seniorityEntityRepository.findByName(seniorityName).get())
                                        .collect(Collectors.toSet());
        Set<SkillEntity> ownedSkills =
                userPreferencesForm.getOwnedSkills().stream()
                                .map(skillDTO -> skillEntityRepository.findByName(skillDTO.getName()).get())
                                        .collect(Collectors.toSet());
        UserPreferencesEntity userPreferences =
                new UserPreferencesEntity(userPreferencesForm, ownedSkills, ownedSeniority, userEntity);

        userEntity.setUserPreferences(userPreferences);
        userEntityRepository.saveAndFlush(userEntity);
    }

    public UserEntity getEntityByEmail(String email) {
        return userEntityRepository.findByEmail(email).orElseThrow((() -> new ValueNotFoundException(email)));
    }

    public UserEntity createEntity(UserRequestBodyModel userRequestBodyModel) {
        UserEntity userEntity = new UserEntity(userRequestBodyModel);
        userEntityRepository.save(userEntity);
        return userEntity;
    }

    public UserEntity getEntityById(String id) {
        return userEntityRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }

    public Set<UserEntity> getAllEntities() {
        return new HashSet<>(userEntityRepository.findAll());
    }

    public void deleteEntityById(String id) {
        userEntityRepository.deleteById(id);
    }

    public UserEntity putEntity(String id, UserRequestBodyModel userRequestBodyModel) {
        UserEntity userEntity = getEntityById(id);
        BeanUtils.copyProperties(userRequestBodyModel, userEntity);
        userEntityRepository.save(userEntity);
        return userEntity;
    }

    public UserEntity patchEntity(String id, Map<Object, Object> fields) {
        UserEntity userEntity = getEntityById(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UnifiedOfferEntity.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, userEntity, value);
        });
        return userEntity;
    }

    public UserEntity addUser(RegisterForm registerForm) {
        UserEntity newUser = new UserEntity(registerForm);
        userEntityRepository.saveAndFlush(newUser);
        return newUser;
    }
}
