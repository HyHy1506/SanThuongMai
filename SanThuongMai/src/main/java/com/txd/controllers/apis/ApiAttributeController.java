package com.txd.controllers.apis;

import com.txd.dto.AttributeDTO;
import com.txd.pojo.Attribute;
import com.txd.services.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiAttributeController {
    
    @Autowired
    private AttributeService attributeService;
    
    @GetMapping("/attributes")
    public ResponseEntity<List<AttributeDTO>> list() {
        List<Attribute> attributes = this.attributeService.getAttributes();
        List<AttributeDTO> attributeDTOs = attributes.stream().map(AttributeDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(attributeDTOs, HttpStatus.OK);
    }
}