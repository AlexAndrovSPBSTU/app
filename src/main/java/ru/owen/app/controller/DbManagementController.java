package ru.owen.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.owen.app.service.DbManagementService;

import java.io.IOException;

@RestController
@RequestMapping("/database")
public class DbManagementController {
    private final DbManagementService dbManagementService;

    @Autowired
    public DbManagementController(DbManagementService dbManagementService) {
        this.dbManagementService = dbManagementService;
    }

    @GetMapping("/clear")
    public ResponseEntity<?> clearDB() {
        dbManagementService.clearDB();
        return ResponseEntity.ok("DB has benn cleared");
    }

    @GetMapping("/reload")
    public ResponseEntity<?> reloadDB() throws IOException {
        dbManagementService.reloadDB();
        return ResponseEntity.ok("Database has been reloaded");
    }


}

