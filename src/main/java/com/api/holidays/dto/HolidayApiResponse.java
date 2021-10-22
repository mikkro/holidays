package com.api.holidays.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HolidayApiResponse {

    @JsonProperty("holidays")
    List<HolidayDto> holidays;
}
