package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.services.IPositionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PositionService implements IPositionService
{
    private final PositionRepos positionRepository;


    public Position getSelectedPosition(int id) {
        return positionRepository.findById(id).orElseThrow();

    public Position getSelectedPosition(int id) throws PositionException {
        return positionRepository.findById(id).orElseThrow(()->new PositionException("The required position not found"));
    }

    public boolean isPresent(int id) throws PositionException {
            Position pos = positionRepository.findById(id).orElseThrow(()->new PositionException("The required position not found"));
            if(pos.getExpireDate().toLocalDate().isBefore(LocalDate.now())){
                return false;
            }
            return true;
    }


    public List<Position> getAllPosition() {
        return  positionRepository.findAll();
    }

    public long getcountPosition(){
        return positionRepository.count();
    }
    public Position createPost(Position post) {
        return positionRepository.save(post);
    }

    public List<Position> searchPositions(String keyword) {
        return positionRepository.findByTitleContaining(keyword);
    }
}
