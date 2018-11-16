package com.zhgame.animalkindom.animal.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimalServiceTest {

    @Autowired
    private AnimalService animalService;

    @Test
    public void metepsychosis() throws Exception {
        System.out.println(animalService.propUpgrade(70, 0));
        System.out.println(animalService.propUpgrade(70, 1));
        System.out.println(animalService.propUpgrade(70, 2));
    }

}