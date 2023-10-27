package com.example.eSmartRecruit.service.impl;

import com.example.eSmartRecruit.models.Positions;
import com.example.eSmartRecruit.repository.PositionRepository;
import com.example.eSmartRecruit.service.IPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService implements IPositionService {
    private final PositionRepository positionRepository;
    public Positions getSelectedPosition(Integer id){
        return positionRepository.findById(id).orElseThrow();
    }
}
