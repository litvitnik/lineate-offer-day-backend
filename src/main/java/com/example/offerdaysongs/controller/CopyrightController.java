package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.dto.CopyrightDto;
import com.example.offerdaysongs.dto.requests.CreateCopyrightRequest;
import com.example.offerdaysongs.exceptions.CopyrightNotFoundException;
import com.example.offerdaysongs.model.Copyright;
import com.example.offerdaysongs.service.CopyrightService;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/copyrights")
public class CopyrightController {

    private static final String ID = "id";
    private final CopyrightService copyrightService;

    public CopyrightController(CopyrightService copyrightService) {
        this.copyrightService = copyrightService;
    }

    @GetMapping("/")
    public List<CopyrightDto> getAll() {
        return copyrightService.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id:[\\d]+}")
    public CopyrightDto get(@PathVariable(ID) long id) {
        Copyright copyright = copyrightService.getById(id);
        return convertToDto(copyright);
    }

    @PostMapping("/")
    public CopyrightDto create(@RequestBody CreateCopyrightRequest request) {
        return convertToDto(copyrightService.create(request));
    }

    @PutMapping("/{id:[\\d]+}")
    public void editCopyright(@PathVariable("id") Long id, @RequestBody CreateCopyrightRequest request){
        copyrightService.update(request, id);
    }

    @GetMapping("/{companyId:[\\d]+}")
    public List<CopyrightDto> getAllByCompanyId(@PathVariable("companyId") Long id){
        return copyrightService.getAllByCompanyId(id).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/period")
    public Copyright getCopyrightByPeriod(@RequestParam ZonedDateTime begins, @RequestParam ZonedDateTime expires){
        return copyrightService.getCopyrightByPeriod(begins, expires);
    }

    private CopyrightDto convertToDto(Copyright copyright){
        return new CopyrightDto(
                copyright.getId(),
                copyright.getBegins(),
                copyright.getExpires(),
                copyright.getFee(),
                copyright.getCompany(),
                copyright.getRecording());
    }
}
