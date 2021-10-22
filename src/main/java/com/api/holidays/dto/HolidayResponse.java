package com.api.holidays.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class HolidayResponse {

    private LocalDate date;
    private String name1;
    private String name2;
}
