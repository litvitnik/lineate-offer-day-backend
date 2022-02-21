package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.dto.RecordingDto;
import com.example.offerdaysongs.dto.SingerDto;
import com.example.offerdaysongs.dto.requests.CreateRecordingRequest;
import com.example.offerdaysongs.exceptions.RecordingNotFoundException;
import com.example.offerdaysongs.model.Copyright;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Singer;
import com.example.offerdaysongs.service.RecordingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recordings")
public class RecordingController {
    private static final String ID = "id";
    private final RecordingService recordingService;

    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @GetMapping("/")
    public List<RecordingDto> getAll(){
        return recordingService.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id:[\\d]+}")
    public RecordingDto get(@PathVariable(ID) long id) {
        Recording recording = recordingService.getById(id);
        return convertToDto(recording);
    }

    @PostMapping("/")
    public RecordingDto create(@RequestBody CreateRecordingRequest request) {
        return convertToDto(recordingService.create(request));
    }

    @GetMapping("/{id}/fee")
    public BigDecimal getFeeSum(@PathVariable Long id){
        return recordingService.getById(id).getCopyrights()
                .stream()
                .map(Copyright::getFee)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private RecordingDto convertToDto(Recording recording)
    {
        Singer singer = recording.getSinger();
        return new RecordingDto(recording.getId(),
                                recording.getTitle(),
                                recording.getVersion(),
                                recording.getReleaseTime(),
                                singer != null ? new SingerDto(singer.getId(), singer.getName()) : null);



    }
}
