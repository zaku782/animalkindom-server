package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.animal.entity.BagItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BagItemRepository extends JpaRepository<BagItem, Long> {
    List<BagItem> getByAnimalId(Long animalId);

    void deleteByAnimalId(Long animalId);
}
