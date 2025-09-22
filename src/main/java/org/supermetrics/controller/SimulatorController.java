package org.supermetrics.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supermetrics.simulator.ReadingsSimulator;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/simulator")
@RequiredArgsConstructor
@Tag(name = "Simulator Controller", description = "API to turn on/off the simulator")
public class SimulatorController {

    private final ReadingsSimulator readingsSimulator;

    @PostMapping("/on")
    public String turnOn() {
        readingsSimulator.start();
        return "Simulator is ON";
    }

    @PostMapping("/off")
    public String turnOff() {
        readingsSimulator.stop();
        return "Simulator is OFF";
    }

}
