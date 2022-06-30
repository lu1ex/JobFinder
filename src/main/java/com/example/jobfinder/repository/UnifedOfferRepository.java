package com.example.jobfinder.repository;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UnifedOfferRepository extends JpaRepository<UnifiedOfferEntity, String> {

    Long deleteAllByExpirationDateBefore(LocalDateTime localDateTime);

    @Query(value = "SELECT o.url FROM UnifiedOfferEntity o WHERE o.url LIKE CONCAT('%',:urlFragment,'%')")
    List<String> findUrls(String urlFragment);

/*    @Query(value = "select * from unified_offers uo" +
            "inner join required_skill rs on uo.id = rs.unique_identifier" +
            "    inner join skills s on rs.skill_id = s.id" +
            "inner join required_seniority r on uo.id = r.unique_identifier" +
            "    inner join  seniority s2 on r.seniority_id = s2.id" +
            "where s.name = 'JAVA' and s2.seniority = 'MID'")
    List<UnifiedOfferEntity> getOffersWhichMatchToSkillAndSeniorityyy();*/

/*    @Query(value = "SELECT (*) FROM UnifiedOfferEntity uo inner join SkillEntity s on uo.uniqueIdentifier = s.id")
    List<UnifiedOfferEntity> getOffersWhichMatchToSkillAndSeniority();*/
}
