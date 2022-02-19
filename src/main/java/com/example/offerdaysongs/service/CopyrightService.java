package com.example.offerdaysongs.service;

import com.example.offerdaysongs.dto.requests.CreateCopyrightRequest;
import com.example.offerdaysongs.exceptions.CopyrightNotFoundException;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Copyright;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.repository.CompanyRepository;
import com.example.offerdaysongs.repository.CopyrightRepository;
import com.example.offerdaysongs.repository.RecordingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CopyrightService {
    private final CopyrightRepository copyrightRepository;
    private final RecordingRepository recordingRepository;
    private final CompanyRepository companyRepository;

    public CopyrightService(CopyrightRepository copyrightRepository, RecordingRepository recordingRepository, CompanyRepository companyRepository) {
        this.copyrightRepository = copyrightRepository;
        this.recordingRepository = recordingRepository;
        this.companyRepository = companyRepository;
    }

    public List<Copyright> getAll() {
        return copyrightRepository.findAll();
    }

    public Copyright getById(long id) {
        return copyrightRepository.findById(id).orElseThrow(CopyrightNotFoundException::new);
    }

    @Transactional
    public Copyright create(CreateCopyrightRequest request) {
        Copyright copyright = new Copyright();
        copyright.setBegins(request.getBegins());
        copyright.setExpires(request.getExpires());
        copyright.setFee(request.getFee());

        Recording recordingDto = request.getRecording();
        if(recordingDto != null){
            Recording recording = recordingRepository.findById(recordingDto.getId()).orElseGet(() -> {
                Recording temp = new Recording();
                temp.setSinger(recordingDto.getSinger());
                temp.setReleaseTime(recordingDto.getReleaseTime());
                temp.setTitle(recordingDto.getTitle());
                temp.setReleaseTime(recordingDto.getReleaseTime());
                return recordingRepository.save(temp);
            });
            copyright.setRecording(recording);
        }

        Company companyDto = request.getCompany();
        if(companyDto != null){
            Company company = companyRepository.findById(companyDto.getId()).orElseGet(() -> {
                Company temp = new Company();
                temp.setName(companyDto.getName());
                return companyRepository.save(temp);
            });
            copyright.setCompany(company);
        }

        return copyrightRepository.save(copyright);
    }

    public void update(CreateCopyrightRequest editedCopyright, long id) {
        Copyright oldCopyRight = copyrightRepository.findById(id).orElseThrow(CopyrightNotFoundException::new);
        oldCopyRight.setId(id);
        copyrightRepository.save(oldCopyRight);
    }

    public List<Copyright> getAllByCompanyId(long id) {
        return copyrightRepository.findAllByCompanyId(id);
    }
    public Copyright getCopyrightByPeriod(ZonedDateTime begins, ZonedDateTime expires) {
        return copyrightRepository.findCopyrightByBeginsAndExpires(begins, expires).orElseThrow(CopyrightNotFoundException::new);
    }
}
