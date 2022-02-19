package com.example.offerdaysongs.dto;

import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Recording;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class CopyrightDto {

    Long id;
    ZonedDateTime begins;
    ZonedDateTime expires;
    BigDecimal fee;
    Company company;
    Recording recording;
}
