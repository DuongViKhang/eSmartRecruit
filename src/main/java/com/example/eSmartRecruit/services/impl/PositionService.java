package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.PositionNotFoundException;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.services.IPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PositionService implements IPositionService {
    private final PositionRepos positionRepository;

    public Position getSelectedPosition(int id) throws PositionNotFoundException {
        return positionRepository.findById(id).orElseThrow(()->new PositionNotFoundException("The required position not found"));
    }

    public boolean isPresent(int id) throws PositionNotFoundException{
        try {
            Position pos = positionRepository.findById(id).orElseThrow(()->new PositionNotFoundException("The required position not found"));
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public List<Position> getAllPosition() {
        return  positionRepository.findAll();
    }

    public List<Position> searchPositions(String keyword) {
        return positionRepository.findByTitleContaining(keyword);
    }
}
