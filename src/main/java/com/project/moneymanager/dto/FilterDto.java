package com.project.moneymanager.dto;


import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto
{
    private String keyword;
    private String type;
    private String sortOrder;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortField;

}
