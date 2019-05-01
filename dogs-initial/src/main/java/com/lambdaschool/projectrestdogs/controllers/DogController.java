package com.lambdaschool.projectrestdogs.controllers;

import com.lambdaschool.projectrestdogs.ProjectrestdogsApplication;
import com.lambdaschool.projectrestdogs.exceptions.ResourceNotFoundException;
import com.lambdaschool.projectrestdogs.models.Dog;
import com.lambdaschool.projectrestdogs.services.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/dogs")
public class DogController
{

    private static final Logger logger = LoggerFactory.getLogger(DogController.class);

    @Autowired
    MessageSender msgSender;

    // localhost:8080/dogs/dogs
    @GetMapping(value = "/dogs")
    public ResponseEntity<?> getAllDogs()
    {
        ArrayList<Dog> dogs = ProjectrestdogsApplication.ourDogList.dogList;
        logger.info("Getting list of dogs from endpoint /dogs");
        logger.debug("list of dogs from /dogs contains " + dogs.size() + " entries");

        msgSender.sendMessage("MY MESSAGE SENDING SERVICE WORKS!!!!!" + dogs.get(0));

        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    // localhost:8080/dogs/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDogDetail(@PathVariable long id)
    {
        logger.info("Getting dog by id from endpoint /dogs/{id}");
        logger.debug("searching for a dog with id: " + id);

        Dog rtnDog = ProjectrestdogsApplication.ourDogList.findDog(d -> (d.getId() == id));

        if (rtnDog == null) {
            throw new ResourceNotFoundException("Could not find dog with id: " + id);
        }

        logger.debug("found dog " + rtnDog);
        return new ResponseEntity<>(rtnDog, HttpStatus.OK);
    }

    // localhost:8080/dogs/breeds/{breed}
    @GetMapping(value = "/breeds/{breed}")
    public ResponseEntity<?> getDogBreeds (@PathVariable String breed)
    {

        logger.info("Getting dogs by breed from endpoint /dogs/breeds/{breed}");
        logger.debug("searching for a dog with breed: " + breed);

        ArrayList<Dog> rtnDogs = ProjectrestdogsApplication.ourDogList.
                findDogs(d -> d.getBreed().toUpperCase().equals(breed.toUpperCase()));

        if (rtnDogs.size() < 1) {
            throw new ResourceNotFoundException("Could not find dogs with breed: " + breed);
        }

        logger.debug("Returning list of dogs with size: " + rtnDogs.size());
        return new ResponseEntity<>(rtnDogs, HttpStatus.OK);
    }
}
