package com.api.holidays.service;

import com.api.holidays.dto.HolidayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;

public interface HolidayService {
    HolidayResponse getHolidaysForCountriesFromDate(String country1, String country2, Date date) throws JsonProcessingException;
}
