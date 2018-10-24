package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal getByAccountId(Long accountId);
}
