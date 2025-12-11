package com.project.moneymanager.repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.project.moneymanager.entity.ExpensesEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<ExpensesEntity, Long> {
    List<ExpensesEntity> findByProfileIdOrderByDateDesc(Long id);
    List<ExpensesEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
    @Query(value="select sum(e.amount) from expenses e where e.profile_id=:profileId",nativeQuery=true)
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);
    List<ExpensesEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );
    List<ExpensesEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

    List<ExpensesEntity> findByProfileIdAndDate(Long profileId,LocalDate date);

}
