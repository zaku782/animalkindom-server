package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.animal.entity.Animal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimalServiceTest {

    @Autowired
    private AnimalService animalService;

    @Test
    public void metepsychosis() throws Exception {

        Map<String, Integer> rec = new HashMap<>();
        int times = 100000;
        IntStream.range(0, times).forEach(i -> {
            Animal animal = animalService.metempsychosis(null);
            rec.merge(animal.getName(), 1, (a, b) -> a + b);
        });
        rec.forEach((k, v) -> System.out.println(k + ":" + (float) v / (float) times));

    }

}