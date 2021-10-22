package com.api.holidays.controller;

import com.api.holidays.service.HolidayService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@Validated
@RequestMapping("/api")
public class HolidayController implements HolidayControllerSwaggerDoc {

    @Autowired
    private HolidayService holidayService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping(value = "/holidays", produces = "application/json")
    @SneakyThrows
    public ResponseEntity getHolidays(@RequestParam Date date,
                                      @RequestParam @Length(min = 2, max = 2) String country1,
                                      @RequestParam @Length(min = 2, max = 2) String country2) {
        log.info("Requested fist common holiday for country {} and {} after date {}",country1, country2, date.toString());
        return ResponseEntity.ok().body(holidayService.getHolidaysForCountriesFromDate(country1, country2, date));
    }
}
