package com.example.offerdaysongs.dto.requests;

import com.example.offerdaysongs.model.Singer;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class CreateRecordingRequest {
    private String title;
    private String version;
    private ZonedDateTime releaseTime;
    private Singer singer;
}
