package com.example.jobfinder.config;


import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.enums.TypeOfContract;
import com.example.jobfinder.facade.justjoinit.EmploymentType;
import com.example.jobfinder.facade.justjoinit.JustJoinItModel;
import com.example.jobfinder.facade.justjoinit.Skill;
import com.example.jobfinder.facade.noFluffJobs.Location;
import com.example.jobfinder.facade.noFluffJobs.NoFluffJobsModel;
import com.example.jobfinder.facade.noFluffJobs.Salary;
import com.example.jobfinder.facade.pracujPL.PracujPLModel;
import com.example.jobfinder.repository.SeniorityEntityRepository;
import com.example.jobfinder.repository.SkillEntityRepository;
import com.example.jobfinder.services.CurrencyConverterService;
import com.example.jobfinder.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
                .addMappings(m -> m.using(noFluffJobsIDgenerator).map(noFluffJobsModel -> noFluffJobsModel,
                        UnifiedOfferEntity::setUniqueIdentifier))
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
                .addMappings(m -> m.using(justJoinIDgenerator).map(justJoinItModel -> justJoinItModel,
                        UnifiedOfferEntity::setUniqueIdentifier))
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
                .addMappings(m -> m.using(pracujPLIDgenerator).map(pracujPLModel -> pracujPLModel,
                        UnifiedOfferEntity::setUniqueIdentifier))

        ;

        return modelMapper;
    }

    //NOFLUFFJOBS

    /*private final Converter<String, Set<SkillEntity>> noFluffJobsSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(String technology) {
            HashSet<SkillEntity> result = new HashSet<>();
            if (technology != null) {
                Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(technology.toUpperCase());
                if (skillEntity.isPresent()) {
                    result.add(skillEntity.get());
                } else {
                    skillEntityRepository.saveAndFlush(new SkillEntity(technology.toUpperCase()));
                }
            }

            if (result.isEmpty() && technology != null) {
                Optional<SkillEntity> skillEntityFromRepository = skillEntityRepository.findByName(technology.toUpperCase());
                result.add(skillEntityFromRepository.get());
            }
            return result;
        }
    };*/
    /*private final Converter<List<String>, Set<SkillEntity>> pracujPlSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<String> requiredSkills) {
            HashSet<SkillEntity> result = new HashSet<>();

            for (String requiredSkill : requiredSkills) {
                for (String skill : listOfSkill) {
                    if (requiredSkill.contains(skill)) {
                        result.add(skillEntityRepository.findByName(skill).get());
                    } else {
                        result.add(skillEntityRepository.findByName("OTHERS").get());
                    }
                }
            }
            return result;
        }
    };*/

    private final Converter<String, Set<SkillEntity>> noFluffJobsSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(String technology) {
            HashSet<SkillEntity> result = new HashSet<>();
            if (technology != null) {
                Optional<SkillEntity> optionalSkill = skillEntityRepository.findByName(technology);
                if (optionalSkill.isPresent()) {
                    result.add(optionalSkill.get());
                } else {
                    result.add(skillEntityRepository.findByName("OTHERS").get());
                }
            }
            return result;
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
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(posted),
                    TimeZone.getDefault().toZoneId()).plusDays(30);

        }
    };

    /*private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            System.out.println("Seniority: " + LocalDateTime.now());
            System.out.println(strings.size());
            HashSet<SeniorityEntity> result = new HashSet<>();
            for (String s : strings) {
                Optional<SeniorityEntity> seniorityEntity = seniorityEntityRepository.findBySeniority(s.toUpperCase());
                if (seniorityEntity.isPresent()) {
                    result.add(seniorityEntity.get());
                } else {
                    System.out.println("coś innego");
                    if (s.equalsIgnoreCase("Trainee")) {
                        s = "JUNIOR";
                    }
                    if (s.equalsIgnoreCase("Expert")) {
                        s = "SENIOR";
                    }
                    Optional<SeniorityEntity> seniority = seniorityEntityRepository.findBySeniority(s.toUpperCase());
                    result.add(seniority.get());
                }
            }
            System.out.println("Seniority end: " + LocalDateTime.now());
            return result;
        }
    };*/
    private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            System.out.println("#" + LocalDateTime.now());
            HashSet<SeniorityEntity> result = new HashSet<>();
            strings.removeIf(s -> s.equals("Trainee") || s.equals("Expert"));

            for (String string : strings) {
                Optional<SeniorityEntity> seniority = seniorityEntityRepository.findByName(string.toUpperCase());
                result.add(seniority.get());
            }

            System.out.println("END" + LocalDateTime.now());
            return result;
        }
    };
    /*
    private final Converter<String, Set<SkillEntity>> noFluffJobsSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(String technology) {
            System.out.println("Skill: " + LocalDateTime.now());
            HashSet<SkillEntity> result = new HashSet<>();
            if (technology != null) {
                Optional<SkillEntity> optionalSkill = skillEntityRepository.findByName(technology);
                if (optionalSkill.isPresent()) {
                    result.add(optionalSkill.get());
                } else {
                    result.add(skillEntityRepository.findByName("OTHERS").get());
                }
            }
            System.out.println("Skill end: " + LocalDateTime.now());
            return result;
        }
    };
     */
    /*private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            HashSet<SeniorityEntity> result = new HashSet<>();
            for (String s : strings) {
                if (s.equalsIgnoreCase("Expert")) {
                   s = "SENIOR";
                }
                if (s.equalsIgnoreCase("Trainee")){
                    s = "JUNIOR";
                }

                Optional<SeniorityEntity> seniorityEntity = seniorityEntityRepository.findBySeniority(s);
                if (seniorityEntity.isPresent()) {
                    result.add(seniorityEntity.get());
                } else {
                    seniorityEntityRepository.saveAndFlush(new SeniorityEntity(s));
                }
            }
            if (result.isEmpty() && !strings.isEmpty()) {
                for (String s : strings) {
                    if (s.equalsIgnoreCase("Expert")) {
                        s = "SENIOR";
                    }
                    if (s.equalsIgnoreCase("Trainee")){
                        s = "JUNIOR";
                    }
                    Optional<SeniorityEntity> seniorityEntityFromRepository = seniorityEntityRepository.findBySeniority(s);
                    result.add(seniorityEntityFromRepository.get());
                }
            }
            return result;
        }
    };*/

   /* private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            HashSet<SeniorityEntity> result = new HashSet<>();
            for (String s : strings) {
                Seniority seniority;
                if (s.equalsIgnoreCase("Expert") || s.equalsIgnoreCase("Senior")) {
                    seniority = Seniority.SENIOR;
                } else if (s.equalsIgnoreCase("Trainee") || s.equalsIgnoreCase("Junior")){
                    seniority = Seniority.JUNIOR;
                } else  {
                    seniority = Seniority.MID;
                }

                Optional<SeniorityEntity> seniorityEntity = seniorityEntityRepository.findBySeniority(seniority);
                if (seniorityEntity.isPresent()) {
                    result.add(seniorityEntity.get());
                } else {
                    seniorityEntityRepository.saveAndFlush(new SeniorityEntity(seniority));
                }
            }

            if (result.isEmpty() && !strings.isEmpty()) {
                Seniority seniority;
                for (String s : strings) {
                    if (s.equalsIgnoreCase("Expert") || s.equalsIgnoreCase("Senior")) {
                        seniority = Seniority.SENIOR;
                    } else if (s.equalsIgnoreCase("Trainee") || s.equalsIgnoreCase("Junior")){
                        seniority = Seniority.JUNIOR;
                    } else  {
                        seniority = Seniority.MID;
                    }
                    Optional<SeniorityEntity> seniorityEntityFromRepository = seniorityEntityRepository.findBySeniority(seniority);
                    result.add(seniorityEntityFromRepository.get());
                }
            }
            return result;
        }
    };*/

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

    private final Converter<NoFluffJobsModel, String> noFluffJobsIDgenerator = new AbstractConverter<>() {
        @Override
        protected String convert(NoFluffJobsModel noFluffJobsModel) {
            return UUID.randomUUID().toString();
        }
    };

    //JustJoinIT
    private final Converter<String, Set<SeniorityEntity>> justJoinItSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(String s) {
            System.out.println("Seniority start" + LocalDateTime.now());
            Set<SeniorityEntity> result = new HashSet<>();
            Optional<SeniorityEntity> seniorityFromRepository = seniorityEntityRepository.findByName(s.toUpperCase());
            seniorityFromRepository.ifPresent(result::add);

            System.out.println("Seniority end" + LocalDateTime.now());
            return result;
        }
    };

    private final Converter<Date, LocalDateTime> justJoinItExpirationDateConverter = new AbstractConverter<>() {
        @Override
        protected LocalDateTime convert(Date date) {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(30);
        }
    };

    /*private final Converter<List<Skill>, Set<SkillEntity>> justJoinItSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<Skill> skills) {
            System.out.println("Skill start" + LocalDateTime.now());
            HashSet<SkillEntity> result = new HashSet<>();
            for (Skill skill : skills) {
                String skillName = skill.getName().toUpperCase();

                Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(skillName);
                if (skillEntity.isPresent()) {
                    result.add(skillEntity.get());
                } else {
                    skillEntityRepository.saveAndFlush(new SkillEntity(skillName.toUpperCase()));
                }
            }

            if (result.isEmpty() && !skills.isEmpty()) {
                for (Skill skill : skills) {
                    Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(skill.getName().toUpperCase());
                    result.add(skillEntity.get());
                }
            }

            return result;
        }
    };*/
    /*
    private final Converter<List<String>, Set<SeniorityEntity>> noFluffJobsSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(List<String> strings) {
            System.out.println("#" + LocalDateTime.now());
            HashSet<SeniorityEntity> result = new HashSet<>();
            strings.removeIf(s -> s.equals("Trainee"));
            strings.removeIf(s -> s.equals("Expert"));

            for (String string : strings) {
                Optional<SeniorityEntity> seniority = seniorityEntityRepository.findBySeniority(string.toUpperCase());
                result.add(seniority.get());
            }

            System.out.println("END" + LocalDateTime.now());
            return result;
        }
    };
     */
    private final Converter<List<Skill>, Set<SkillEntity>> justJoinItSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<Skill> skills) {
            System.out.println("Skill start" + LocalDateTime.now());
            HashSet<SkillEntity> result = new HashSet<>();
            for (Skill skill : skills) {
                Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(skill.getName().toUpperCase());
                if (skillEntity.isPresent()) {
                    result.add(skillEntity.get());
                } else {
                    result.add(skillEntityRepository.findByName("OTHERS").get());
                }
            }
            System.out.println("Skill end" + LocalDateTime.now());

            return result;
        }
    };

    private final Converter<List<EmploymentType>, Set<ContractDetailsEntity>> justJoinItContractDetailsConverter =
            new AbstractConverter<>() {
                @Override
                protected Set<ContractDetailsEntity> convert(List<EmploymentType> employmentTypes) {
                    System.out.println("CD start" + LocalDateTime.now());

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
                    System.out.println("CD end" + LocalDateTime.now());


                    return contractDetails;
                }
            };

    private final Converter<String, String> justJoinItUrlConverter = new AbstractConverter<>() {
        @Override
        protected String convert(String source) {
            return "https://justjoin.it/offers/" + source;
        }
    };

    private final Converter<JustJoinItModel, String> justJoinIDgenerator = new AbstractConverter<>() {
        @Override
        protected String convert(JustJoinItModel justJoinItModel) {
            return UUID.randomUUID().toString();
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

    /*
    private final Converter<String, Set<SkillEntity>> noFluffJobsSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(String technology) {
            HashSet<SkillEntity> result = new HashSet<>();
            if (technology != null) {
                Optional<SkillEntity> optionalSkill = skillEntityRepository.findByName(technology);
                if (optionalSkill.isPresent()) {
                    result.add(optionalSkill.get());
                } else {
                    result.add(skillEntityRepository.findByName("OTHERS").get());
                }
            }
            return result;
        }
    };

     */
    private final Converter<String, Set<SeniorityEntity>> pracujPLSeniorityConverter = new AbstractConverter<>() {
        @Override
        protected Set<SeniorityEntity> convert(String employmentLevel) {

            System.out.println("pracuj start seniority:" + LocalDateTime.now());
            String[] senioritiesFromPracujPLModel = employmentLevel.split(",");
            HashSet<SeniorityEntity> seniorities = new HashSet<>();
            for (String s : senioritiesFromPracujPLModel) {
                if (s.toUpperCase().contains("MID")) {
                    s = "MID";
                } else if (s.toUpperCase().contains("JUNIOR")) {
                    s = "JUNIOR";
                } else {
                    s = "SENIOR";
                }
                Optional<SeniorityEntity> seniorityEntity = seniorityEntityRepository.findByName(s);
                seniorityEntity.ifPresent(seniorities::add);
            }

            System.out.println("pracuj end seniority:" + LocalDateTime.now());
            return seniorities;
        }
    };


    private final Converter<PracujPLModel, Set<ContractDetailsEntity>> pracujPLContractDetailsConverter = new AbstractConverter<>() {
        @SneakyThrows
        @Override
        protected Set<ContractDetailsEntity> convert(PracujPLModel pracujPLModel) {

            System.out.println("pracuj start CD:" + LocalDateTime.now());
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

            System.out.println("pracuj end cd:" + LocalDateTime.now());
            return contractDetails;
        }
    };

    /*private final Converter<List<String>, Set<SkillEntity>> pracujPlSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<String> objects) {
            HashSet<SkillEntity> skills = new HashSet<>();
            for (String exptectedTechnology : objects) {
                if (exptectedTechnology != null) {
                    exptectedTechnology = exptectedTechnology.toUpperCase();
                    Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(exptectedTechnology);
                    if (skillEntity.isPresent()) {
                        skills.add(skillEntity.get());
                    } else {
                        skillEntityRepository.saveAndFlush(new SkillEntity(exptectedTechnology));
                    }
                }
            }

            if (skills.isEmpty() && !objects.isEmpty()) {
                for (String object : objects) {
                    Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(object);
                    skills.add(skillEntity.get());
                }
            }
            return skills;
        }
    };
*/
    private final Converter<List<String>, Set<SkillEntity>> pracujPlSkillConverter = new AbstractConverter<>() {
        @Override
        protected Set<SkillEntity> convert(List<String> requiredSkills) {
            System.out.println("pracuj start:" + LocalDateTime.now());
            HashSet<SkillEntity> result = new HashSet<>();

            for (String requiredSkill : requiredSkills) {
                Optional<SkillEntity> skillEntity = skillEntityRepository.findByName(requiredSkill);
                if (skillEntity.isPresent()) {
                    result.add(skillEntity.get());
                } else {
                    result.add(skillEntityRepository.findByName("OTHERS").get());
                }
            }

            System.out.println("pracuj end:" + LocalDateTime.now());
            return result;
        }
    };

    private final Converter<PracujPLModel, String> pracujPLIDgenerator = new AbstractConverter<>() {
        @Override
        protected String convert(PracujPLModel pracujPLModel) {
            return UUID.randomUUID().toString();
        }
    };


    // spring actuator <- przeczytać
    // pełny crud
    // swagger oparty o open api 3
    // git -> merge requesty, pull, push, commit, rozwiazywanie konfliktów
}

