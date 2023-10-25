package com.example.eSmartRecruit.service;

import com.example.eSmartRecruit.models.Positions;
import com.example.eSmartRecruit.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    public Positions getSelectedPosition(Integer id){
        return positionRepository.findById(id).orElseThrow();
    }
}
