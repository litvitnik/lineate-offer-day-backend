package com.example.offerdaysongs.dto.requests;

import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Recording;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CreateCopyrightRequest {
    ZonedDateTime begins;
    ZonedDateTime expires;
    BigDecimal fee;
    Company company;
    Recording recording;
}
