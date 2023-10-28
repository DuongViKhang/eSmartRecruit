package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.services.IPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService implements IPositionService {
    private PositionRepos positionRepository;
    public Position getSelectedPosition(int id){
        return positionRepository.findById(id).orElseThrow();
    }
}
