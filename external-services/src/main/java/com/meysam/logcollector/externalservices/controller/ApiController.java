package com.meysam.logcollector.externalservices.controller;

import com.meysam.logcollector.externalservices.model.AddLogRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class ApiController {

    int c = 0;

    @PostMapping
    public ResponseEntity<String> testExternalApi(@RequestBody AddLogRequestDto addLogRequestDto){

        if(c<4){
            c++;
            return ResponseEntity.internalServerError().body("Server Error");
        }else {
            return ResponseEntity.ok("Successfully received log");
        }

    }
}
