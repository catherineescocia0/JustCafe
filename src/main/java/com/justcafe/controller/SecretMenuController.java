package com.justcafe.controller;

import com.justcafe.repository.SecretCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@Controller
public class SecretMenuController {

    @Autowired
    private SecretCodeRepository secretCodeRepository;

    /** Validate the secret code — returns JSON so JS can handle it */
    @PostMapping("/api/secret/validate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateCode(@RequestBody Map<String, String> body) {
        String code = body.getOrDefault("code", "").trim();
        Map<String, Object> res = new HashMap<>();

        boolean valid = secretCodeRepository
                .findByCodeAndActive(code, true)
                .isPresent();

        res.put("valid", valid);
        if (valid) {
            res.put("redirect", "/secret-menu");
        } else {
            res.put("message", "Invalid code. Try again.");
        }
        return ResponseEntity.ok(res);
    }

    /** The secret dedication page */
    @GetMapping("/secret-menu")
    public String secretMenu(Model model) {
        return "secret-menu";
    }
}
