package com.example.offerdaysongs.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
public class Copyright {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    ZonedDateTime begins;

    ZonedDateTime expires;

    BigDecimal fee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recording_id", insertable = false, updatable = false)
    Recording recording;
}
