package org.supermetrics.controller;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.supermetrics.model.Device;
import org.supermetrics.model.enums.DeviceType;
import org.supermetrics.repository.DeviceRepository;
import org.supermetrics.repository.ReadingRepository;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6").withReuse(true);

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected ReadingRepository readingRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        deviceRepository.deleteAll();
        readingRepository.deleteAll();
    }

    protected RequestSpecification givenUserAuth() {
        return RestAssured.given()
                .auth().preemptive().basic("user", "password")
                .contentType(ContentType.JSON);
    }

    protected RequestSpecification givenDeviceAuth() {
        return RestAssured.given()
                .auth().preemptive().basic("device", "device")
                .contentType(ContentType.JSON);
    }

    protected Device createTestDevice(String name, String zone) {
        Device device = Device.builder()
                .name(name)
                .serialNumber("SN-" + UUID.randomUUID())
                .type(DeviceType.HEART_RATE_MONITOR)
                .zone(zone)
                .brand("Test Brand")
                .active(true)
                .build();
        return deviceRepository.save(device);
    }
}
