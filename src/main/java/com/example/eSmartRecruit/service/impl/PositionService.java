package com.example.eSmartRecruit.service.impl;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.service.IPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService implements IPositionService {
    private final PositionRepos positionRepository;
    public Position getSelectedPosition(int id){
        return positionRepository.findById(id).orElseThrow();
    }
}
