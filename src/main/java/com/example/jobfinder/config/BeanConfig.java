package com.example.jobfinder.config;


import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.enums.Seniority;
import com.example.jobfinder.enums.TypeOfContract;
import com.example.jobfinder.facade.justjoinit.EmploymentType;
import com.example.jobfinder.facade.justjoinit.JustJoinItModel;
import com.example.jobfinder.facade.justjoinit.Skill;
import com.example.jobfinder.facade.noFluffJobs.Location;
import com.example.jobfinder.facade.noFluffJobs.NoFluffJobsModel;
import com.example.jobfinder.facade.noFluffJobs.Salary;
import com.example.jobfinder.facade.pracujPL.PracujPLModel;
import com.example.jobfinder.modelDTO.SkillDTO;
import com.example.jobfinder.modelDTO.UnifiedOfferDTO;
import com.example.jobfinder.repository.SeniorityEntityRepository;
import com.example.jobfinder.repository.SkillEntityRepository;
import com.example.jobfinder.services.CurrencyConverterService;
import com.example.jobfinder.services.HelperService;
import com.example.jobfinder.servicesForDownloaders.SkillHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final SkillEntityRepository skillEntityRepository;
    private final SeniorityEntityRepository seniorityEntityRepository;
    private final CurrencyConverterService currencyConverter;
    private final SkillHelper skillHelper;
    private final HelperService helperService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        /*
        noFluffJobs
         */

        PropertyMap<NoFluffJobsModel, UnifiedOfferEntity> noFluffJobsPropertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setCompanyName(source.getName());
                map().setRemote(source.isFullyRemote());
                map().setRemoteRecruitment(source.isOnlineInterviewAvailable());
            }
        };

        modelMapper.addMappings(noFluffJobsPropertyMap);

        modelMapper.typeMap(NoFluffJobsModel.class, UnifiedOfferEntity.class)
                .addMappings(m -> m.using(noFluffJobsSkillConverter).map(NoFluffJobsModel::getTechnology,
                        UnifiedOfferEntity::setSkills))
                .addMappings(m -> m.using(noFluffJobsCityConverter).map(NoFluffJobsModel::getLocation,
                        UnifiedOfferEntity::setCity))
                .addMappings(m -> m.using(noFluffJobsStreetConverter).map(NoFluffJobsModel::getLocation,
                        UnifiedOfferEntity::setStreet))
                .addMappings(m -> m.using(noFluffJobsExpirationDateConverter).map(NoFluffJobsModel::getPosted,
                        UnifiedOfferEntity::setExpirationDate))
                .addMappings(m -> m.using(noFluffJobsSeniorityConverter).map(NoFluffJobsModel::getSeniority,
                        UnifiedOfferEntity::setSeniority))
                .addMappings(m -> m.using(noFluffJobsContractDetailsConverter).map(NoFluffJobsModel::getSalary,
                        UnifiedOfferEntity::setContractDetails))
                .addMappings(m -> m.using(noFluffJobsUrlConverter).map(NoFluffJobsModel::getUrl,
                        UnifiedOfferEntity::setUrl))
        ;

        /*
         justJoinIT
         */

        PropertyMap<JustJoinItModel, UnifiedOfferEntity> justJoinITPropertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setRemoteRecruitment(source.isRemoteInterview());
            }
        };
        modelMapper.addMappings(justJoinITPropertyMap);

        modelMapper.typeMap(JustJoinItModel.class, UnifiedOfferEntity.class)
                .addMappings(m -> m.using(justJoinItSeniorityConverter).map(JustJoinItModel::getExperienceLevel,
                        UnifiedOfferEntity::setSeniority))
                .addMappings(m -> m.using(justJoinItExpirationDateConverter).map(JustJoinItModel::getPublishedAt,
                        UnifiedOfferEntity::setExpirationDate))
                .addMappings(m -> m.using(justJoinItSkillConverter).map(JustJoinItModel::getSkills,
                        UnifiedOfferEntity::setSkills))
                .addMappings(m -> m.using(justJoinItContractDetailsConverter).map(JustJoinItModel::getEmploymentTypes,
                        UnifiedOfferEntity::setContractDetails))
                .addMappings(m -> m.using(justJoinItUrlConverter).map(JustJoinItModel::getId,
                        UnifiedOfferEntity::setUrl))
        ;

        /*
         pracujPl
         */

        PropertyMap<PracujPLModel, UnifiedOfferEntity> pracujPlPropertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setCompanyName(source.getEmployer());
                map().setRemote(source.isRemoteWork());
                map().setRemoteRecruitment(source.isRemoteRecruitment());
                map().setTitle(source.getJobTitle());
                map().setUrl(source.getOfferUrl());
            }
        };

        modelMapper.addMappings(pracujPlPropertyMap);

        modelMapper.typeMap(PracujPLModel.class, UnifiedOfferEntity.class)
                .addMappings(m -> m.using(pracujPlSkillConverter).map(PracujPLModel::getTechnologiesExpected,
                        UnifiedOfferEntity::setSkills))
                .addMappings(m -> m.using(pracujPLExpirationDate).map(PracujPLModel::getExpirationDate, UnifiedOfferEntity::setExpirationDate))
                .addMappings(m -> m.using(pracujPLCityConverter).map(pracujPLModel -> pracujPLModel, UnifiedOfferEntity::setCity))
                .addMappings(m -> m.using(pracujPLSeniorityConverter).map(PracujPLModel::getEmploymentLevel,
                        UnifiedOfferEntity::setSeniority))
                .addMappings(m -> m.using(pracujPLContractDetailsConverter).map(pracujPLModel -> pracujPLModel,
                        UnifiedOfferEntity::setContractDetails))
        ;

        return modelMapper;
    }

    private final Converter<SkillEntity, Set<UnifiedOfferDTO>> skillEntityUnifiedOffersListConverter =
            new AbstractConverter<>() {
                @Override
                protected Set<UnifiedOfferDTO> convert(SkillEntity skillEntity) {
                    Set<UnifiedOfferDTO> result = new HashSet<>();
                    for (UnifiedOfferEntity unifiedOfferEntity : skillEntity.getUnifiedOffers()) {
                        UnifiedOfferDTO unifiedOfferDTO = new UnifiedOfferDTO();
                        unifiedOfferDTO.setCompanyName(unifiedOfferEntity.getCompanyName());
                        unifiedOfferDTO.setTitle(unifiedOfferEntity.getTitle());

                        result.add(unifiedOfferDTO);
                    }
                    return result;
                }
            };

    private final Converter<String, Set<SkillEntity>> noFluffJobsSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(String technology) {
            HashSet<SkillEntity> skills = new HashSet<>();
            if (technology != null) {
                technology = technology.toUpperCase();
                if (skillHelper.getSkillsThatHaveAlreadyOccurred().contains(technology)) {
                    Optional<SkillEntity> skillFromRepository = skillEntityRepository.findByName(technology);
                    skillFromRepository.ifPresent(skills::add);
                } else {
                    skillHelper.getSkillsThatHaveAlreadyOccurred().add(technology);
                    SkillEntity skillEntity = new SkillEntity(technology.toUpperCase());
                    skills.add(skillEntity);
                }
            }
            return skills;
        }
    };

    private final Converter<String, String> noFluffJobsUrlConverter = new AbstractConverter<>() {
        @Override
        protected String convert(String source) {
            return "https://nofluffjobs.com/pl/job/" + source;
        }
    };

    private final Converter<Location, String> noFluffJobsCityConverter = context ->
            context.getSource().getPlaces().get(0).getCity();

    private final Converter<Location, String> noFluffJobsStreetConverter = context ->
            context.getSource().getPlaces().get(0).getStreet();


    private final Converter<Long, LocalDateTime> noFluffJobsExpirationDateConverter = new AbstractConverter<>() {
        @Override
        protected LocalDateTime convert(Long posted) {
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(posted),
                            TimeZone.getDefault().toZoneId());
            return triggerTime.plusDays(30);

        }
    };

    private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            Set<SeniorityEntity> seniorityEntities = new HashSet<>();
            for (String s : strings) {
                if (s.equalsIgnoreCase("Expert")) {
                    Optional<SeniorityEntity> seniorityFromRepository = seniorityEntityRepository.findBySeniority(Seniority.SENIOR);
                    if (seniorityFromRepository.isPresent()) {
                        seniorityEntities.add(seniorityFromRepository.get());
                    } else {
                        SeniorityEntity seniority = new SeniorityEntity(Seniority.SENIOR);
                        seniorityEntityRepository.save(seniority);
                        seniorityEntities.add(seniority);
                    }
                }
                else if (s.equalsIgnoreCase("Trainee")) {
                    Optional<SeniorityEntity> seniorityFromRepository = seniorityEntityRepository.findBySeniority(Seniority.JUNIOR);
                    if (seniorityFromRepository.isPresent()) {
                        seniorityEntities.add(seniorityFromRepository.get());
                    } else {
                        SeniorityEntity seniority = new SeniorityEntity(Seniority.JUNIOR);
                        seniorityEntityRepository.save(seniority);
                        seniorityEntities.add(seniority);
                    }
                }
                else {
                    Optional<SeniorityEntity> seniorityFromRepository = seniorityEntityRepository.findBySeniority(Seniority.valueOf(s.toUpperCase()));
                    if (seniorityFromRepository.isPresent()) {
                        seniorityEntities.add(seniorityFromRepository.get());
                    } else {
                        SeniorityEntity seniority = new SeniorityEntity(Seniority.valueOf(s));
                        seniorityEntityRepository.save(seniority);
                        seniorityEntities.add(seniority);
                    }
                }
            }
            return seniorityEntities;
        }
    };

    private final Converter<Salary, Set<ContractDetailsEntity>> noFluffJobsContractDetailsConverter = new AbstractConverter<>() {
        @Override
        protected Set<ContractDetailsEntity> convert(Salary salary) {
            Set<ContractDetailsEntity> contractDetails = new HashSet<>();
            double salaryFrom = salary.getFrom();
            double salaryTo = salary.getMyto();
            if (!salary.currency.equalsIgnoreCase("PLN")) {
                salaryFrom = currencyConverter.convert(salaryFrom, salary.getCurrency());
                salaryTo = currencyConverter.convert(salaryTo, salary.getCurrency());
            }
            if (salary.getType().equals("b2b")) {
                contractDetails.add(new ContractDetailsEntity(TypeOfContract.B2B, salaryFrom, salaryTo));
            } else {
                contractDetails.add(new ContractDetailsEntity(TypeOfContract.UOP, salaryFrom, salaryTo));
            }
            return contractDetails;
        }
    };

    //JOSTJOINIT
    private final Converter<String, Set<SeniorityEntity>> justJoinItSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(String s) {
            Set<SeniorityEntity> seniorityEntities = new HashSet<>();
            Optional<SeniorityEntity> seniorityFromRepository =
                    seniorityEntityRepository.findBySeniority(Seniority.valueOf(s.toUpperCase()));
            if (seniorityFromRepository.isPresent()) {
                seniorityEntities.add(seniorityFromRepository.get());
            } else {
                seniorityEntities.add(new SeniorityEntity(Seniority.valueOf(s.toUpperCase())));
            }
            return seniorityEntities;
        }
    };

    private final Converter<Date, LocalDateTime> justJoinItExpirationDateConverter = new AbstractConverter<>() {
        @Override
        protected LocalDateTime convert(Date date) {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(30);
        }
    };

    private final Converter<List<Skill>, Set<SkillEntity>> justJoinItSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<Skill> skills) {
            Set<SkillEntity> result = new HashSet<>();
            for (Skill skill : skills) {
                String skillName = skill.getName().toUpperCase();
                if (skillHelper.getSkillsThatHaveAlreadyOccurred().contains(skillName)) {
                    Optional<SkillEntity> skillFromRepository = skillEntityRepository.findByName(skillName);
                    skillFromRepository.ifPresent(result::add);
                } else {
                    skillHelper.getSkillsThatHaveAlreadyOccurred().add(skillName);
                    SkillEntity skillEntity = new SkillEntity(skillName);
                    result.add(skillEntity);
                }
            }
            return result;
        }
    };

    private final Converter<List<EmploymentType>, Set<ContractDetailsEntity>> justJoinItContractDetailsConverter =
            new AbstractConverter<>() {
                @Override
                protected Set<ContractDetailsEntity> convert(List<EmploymentType> employmentTypes) {
                    Set<ContractDetailsEntity> contractDetails = new HashSet<>();
                    double salaryFrom;
                    double salaryTo;
                    for (EmploymentType employmentType : employmentTypes.stream().filter(Objects::nonNull).collect(Collectors.toList())) {
                        if (employmentType.getSalary() != null &&
                                employmentType.getSalary().getCurrency().equalsIgnoreCase("pln")) {
                            salaryFrom = employmentType.getSalary().getFrom();
                            salaryTo = employmentType.getSalary().getMyto();
                        } else {
                            salaryFrom = currencyConverter.convert(
                                    Optional.ofNullable(employmentType.getSalary())
                                            .map(com.example.jobfinder.facade.justjoinit.Salary::getFrom).orElse(0),
                                    Optional.ofNullable(employmentType.getSalary())
                                            .map(com.example.jobfinder.facade.justjoinit.Salary::getCurrency).orElse("PLN")
                            );
                            salaryTo = currencyConverter.convert(
                                    Optional.ofNullable(employmentType.getSalary())
                                            .map(com.example.jobfinder.facade.justjoinit.Salary::getMyto).orElse(0),
                                    Optional.ofNullable(employmentType.getSalary())
                                            .map(com.example.jobfinder.facade.justjoinit.Salary::getCurrency).orElse("PLN")
                            );
                        }
                        if (employmentType.getType().equals("b2b")) {
                            contractDetails.add(new ContractDetailsEntity(TypeOfContract.B2B,
                                    salaryFrom, salaryTo));
                        } else {
                            contractDetails.add(new ContractDetailsEntity(TypeOfContract.UOP,
                                    salaryFrom, salaryTo));
                        }
                    }
                    return contractDetails;
                }
            };

    private final Converter<String, String> justJoinItUrlConverter = new AbstractConverter<>() {
        @Override
        protected String convert(String source) {
            return "https://justjoin.it/offers/" + source;
        }
    };

    //PRACUJ.PL

    private final Converter<LocalDate, LocalDateTime> pracujPLExpirationDate = context ->
            LocalDateTime.of(context.getSource(), LocalTime.of(0, 0));

    private final Converter<PracujPLModel, String> pracujPLCityConverter = context -> {
        Pattern pattern = Pattern.compile("\\-[a-z]*\\,");
        Matcher matcher = pattern.matcher(context.getSource().getOfferUrl());

        if (matcher.find()) {
            String city = matcher.group(0);
            city = city.replaceAll("-", "").replaceAll(",", "");
            return StringUtils.capitalize(city);
        } else {
            return context.getSource().getLocation();
        }
    };

    private final Converter<String, Set<SeniorityEntity>> pracujPLSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(String employmentLevel) {
            String[] senioritiesFromPracujPLModel = employmentLevel.split(",");
            Set<SeniorityEntity> seniorityEntities = new HashSet<>();
            for (String s : senioritiesFromPracujPLModel) {
                Pattern pattern = Pattern.compile("\\([A-Z|a-z]+");
                Matcher matcher = pattern.matcher(s);
                String employmentType = "";
                if (matcher.find()) {
                    employmentType = matcher.group(0);
                }
                employmentType = employmentType.replaceAll("\\(", "").replaceAll(" ", "").toUpperCase();
                if (!employmentType.equals("")) {
                    Optional<SeniorityEntity> seniorityFromRepository = seniorityEntityRepository.findBySeniority(Seniority.valueOf(employmentType));
                    if (seniorityFromRepository.isPresent()) {
                        seniorityEntities.add(seniorityFromRepository.get());
                    } else {
                        SeniorityEntity seniority = new SeniorityEntity(Seniority.valueOf(employmentType));
                        seniorityEntityRepository.saveAndFlush(seniority);
                        seniorityEntities.add(seniority);
                    }
                }
            }
            return seniorityEntities;
        }
    };


    private final Converter<PracujPLModel, Set<ContractDetailsEntity>> pracujPLContractDetailsConverter = new AbstractConverter<>() {
        @SneakyThrows
        @Override
        protected Set<ContractDetailsEntity> convert(PracujPLModel pracujPLModel) {
            Set<ContractDetailsEntity> contractDetails = new HashSet<>();
            String salaryFromStringValue = "0";
            String salaryToStringValue;
            String salaryFromPracujPL = pracujPLModel.getSalary().replaceAll(" ", "");


            if (salaryFromPracujPL.contains("-")) {
                String[] splited = salaryFromPracujPL.split("-");
                salaryFromStringValue = splited[0];

                salaryToStringValue = StringUtils.substringBefore(splited[1], "zł");
                salaryToStringValue = salaryToStringValue.replaceAll("-", "").replaceAll("\\s+", "");
            } else {
                Pattern salaryFromPattern = Pattern.compile("[0-9| ]*");
                Matcher matcherFrom = salaryFromPattern.matcher(salaryFromPracujPL);
                if (matcherFrom.find()) {
                    salaryFromStringValue = matcherFrom.group();
                }

                salaryFromStringValue = salaryFromStringValue.replaceAll("-", ""); //.replaceAll("\\s+", "");

                salaryToStringValue = salaryFromStringValue;
            }

            double salaryFrom = DecimalFormat.getNumberInstance().parse(salaryFromStringValue).doubleValue();
            double salaryTo = DecimalFormat.getNumberInstance().parse(salaryToStringValue).doubleValue();

            if (pracujPLModel.getSalary().contains("godz")) {
                salaryFrom *= 168;
                salaryTo *= 168;
            } else if (salaryFrom < 1000) {
                salaryFrom *= 168;
            }

            if (pracujPLModel.getSalary().contains("netto")) {
                salaryFrom *= 1.08;
                salaryTo *= 1.08;
                salaryFrom = Math.round(salaryFrom);
                salaryTo = Math.round(salaryTo);
            }

            for (String s : pracujPLModel.getTypesOfContract()) {
                if (s.contains("B2B")) {
                    contractDetails.add(new ContractDetailsEntity(TypeOfContract.B2B, salaryFrom, salaryTo));
                } else {
                    contractDetails.add(new ContractDetailsEntity(TypeOfContract.UOP, salaryFrom, salaryTo));
                }
            }
            return contractDetails;
        }
    };

    private final Converter<List<String>, Set<SkillEntity>> pracujPlSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<String> objects) {
            HashSet<SkillEntity> skills = new HashSet<>();
            for (String exptectedTechnology : objects) {
                if (exptectedTechnology != null) {
                    exptectedTechnology = exptectedTechnology.toUpperCase();
                    if (skillHelper.getSkillsThatHaveAlreadyOccurred().contains(exptectedTechnology)) {
                        Optional<SkillEntity> skillFromRepository = skillEntityRepository.findByName(exptectedTechnology);
                        skillFromRepository.ifPresent(skills::add);
                    } else {
                        skillHelper.getSkillsThatHaveAlreadyOccurred().add(exptectedTechnology);
                        SkillEntity skillEntity = new SkillEntity(exptectedTechnology);
                        skills.add(skillEntity);
                    }
                }

            }
            return skills;
        }
    };


    // spring actuator <- przeczytać
    // pełny crud
    // swagger oparty o open api 3
    // git -> merge requesty, pull, push, commit, rozwiazywanie konfliktów
}

