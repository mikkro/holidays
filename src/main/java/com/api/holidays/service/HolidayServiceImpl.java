package com.api.holidays.service;

import com.api.holidays.dto.HolidayApiResponse;
import com.api.holidays.dto.HolidayDto;
import com.api.holidays.dto.HolidayResponse;
import com.api.holidays.exception.CommonHolidaysNotFoundException;
import com.api.holidays.exception.CountryValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class HolidayServiceImpl implements HolidayService {


    @Value("${holidays.api.key}")
    private String apiKey;

    @Value("${holidays.api.url}")
    private String apiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public HolidayResponse getHolidaysForCountriesFromDate(String country1, String country2, Date date) {

        validateCountries(country1, country2);

        List<HolidayDto> country1Holidays = getHolidaysForCountry(country1, date);
        List<HolidayDto> country2Holidays = getHolidaysForCountry(country2, date);

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Optional<HolidayDto> country1HolidayOptional = country1Holidays
                .stream()
                .filter(country1Holiday -> country1Holiday.getDate().isAfter(localDate))
                .filter(country1Holiday -> findHolidayInSecondCountry(country1Holiday, country2Holidays))
                .findFirst();

        if (country1HolidayOptional.isPresent()) {
            final HolidayDto country1Holiday = country1HolidayOptional.get();

            final HolidayDto country2Holiday = country2Holidays
                    .stream()
                    .filter(holiday -> holiday.getDate().isEqual(country1Holiday.getDate()))
                    .findFirst()
                    .get(); //we can use get because we already found that there is a holiday with the same date
            return HolidayResponse.builder().date(country1Holiday.getDate()).name1(country1Holiday.getName()).name2(country2Holiday.getName()).build();
        }

        throw new CommonHolidaysNotFoundException(String.format("Not found common holidays for country: %s and country: %s from date : %d-%d-%d", country1, country2, localDate.getYear(), localDate.getMonth().getValue(), localDate.getDayOfMonth()));

    }

    private boolean findHolidayInSecondCountry(HolidayDto country1Holiday, List<HolidayDto> country2Holidays) {
        return country2Holidays
                .stream()
                .anyMatch(country2Holiday -> country2Holiday.getDate().isEqual(country1Holiday.getDate()));
    }


    @SneakyThrows
    private List<HolidayDto> getHolidaysForCountry(String country1, Date date) {
        String holidays = callGetMethod(apiUrl, String.class, apiKey, country1, getYearFromDate(date));
        return objectMapper.readValue(holidays, HolidayApiResponse.class).getHolidays();
    }

    private int getYearFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    private void validateCountries(String country1, String country2) {
        //Chosen Api contains holidays for all iso countries
        List<String> isoCountries = Arrays.asList(Locale.getISOCountries());
        validateCountry(isoCountries, country1);
        validateCountry(isoCountries, country2);
    }

    private void validateCountry(List<String> isoCountries, String country) {
        if (!isoCountries.contains(country)) {
            throw new CountryValidationException(String.format("Api doesn't support country: %s", country));
        }
    }

    private <T> T callGetMethod(String url, Class<T> responseType, Object... objects) {
        return restTemplate.getForObject(url,
                responseType, objects);
    }
}