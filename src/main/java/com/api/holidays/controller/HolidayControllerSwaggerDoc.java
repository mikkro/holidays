package com.api.holidays.controller;

import com.api.holidays.dto.HolidayResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

public interface HolidayControllerSwaggerDoc {

    @ApiOperation(value = "Get holiday after the given date that will happen on the same day in both countries.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Holiday after the given date that will happen on the same day in both countries.", response = HolidayResponse.class),
            @ApiResponse(code = 400, message = "In case of validation exception."),
            @ApiResponse(code = 401, message = "In case of unauthorized access."),
            @ApiResponse(code = 403, message = "In case of forbidden action."),
            @ApiResponse(code = 404, message = "In case of not found resource."),

    })
    ResponseEntity getHolidays(@RequestParam Date date,
                               @RequestParam @Length(min = 2, max = 2) String country1,
                               @RequestParam @Length(min = 2, max = 2) String country2);
}
