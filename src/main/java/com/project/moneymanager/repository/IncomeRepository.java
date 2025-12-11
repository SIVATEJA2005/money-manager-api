package com.project.moneymanager.repository;
import com.project.moneymanager.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public interface IncomeRepository extends JpaRepository<IncomeEntity,Long>
{
        List<IncomeEntity> findByProfileIdOrderByDateDesc(Long id);
        List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
        @Query(value="Select sum(e.amount) from income e where e.profile_id=:profileId",nativeQuery=true)
        BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);
        List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                Long profileId,
                LocalDate startDate,
                LocalDate endDate,
                String keyword,
                Sort sort
        );
        List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

}



