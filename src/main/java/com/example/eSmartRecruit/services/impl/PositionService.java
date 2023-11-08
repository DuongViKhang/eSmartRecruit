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


    public List<Position> getAllPosition() throws PositionException {
        return  positionRepository.findAll();
    }

    public List<Position> searchPositions(String keyword) throws Exception {
        return positionRepository.findByTitleContaining(keyword);
    }
    public String editPosition(Integer positionID, Position position) {
        try {
            Position existingPosition = positionRepository.findById(positionID)
                    .orElseThrow(() -> new PositionException("The required position not found"));

            existingPosition.setTitle(position.getTitle());
            existingPosition.setJobDescription(position.getJobDescription());
            existingPosition.setJobRequirements(position.getJobRequirements());
            existingPosition.setSalary(position.getSalary());
            existingPosition.setExpireDate(position.getExpireDate());
            existingPosition.setLocation(position.getLocation());
            positionRepository.save(existingPosition);

            return "Position updated successfully";
        } catch (PositionException e) {
            return "Error updating position: " + e.getMessage();
        }
    }

    public void deletePosition(Integer positionID) throws PositionException {
        Position existingPosition = positionRepository.findById(positionID).orElseThrow(() -> new PositionException("Position not found"));
        positionRepository.delete(existingPosition);
    }

}
