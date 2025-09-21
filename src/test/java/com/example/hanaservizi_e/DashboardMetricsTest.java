package com.example.hanaservizi_e;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Epic("Dashboard")
@Feature("Métricas")
public class DashboardMetricsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Story("Obtener métricas del dashboard")
    @Description("Verifica que el endpoint /api/dashboard/metrics devuelve datos válidos")
    @Severity(SeverityLevel.CRITICAL)
    public void testMetricsEndpoint() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/dashboard/metrics", String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("totalUsers");

        // Extraer el valor de totalUsers de forma segura, recibiendo el integer de la API
        Number totalUsers = JsonPath.read(response.getBody(), "$.totalUsers");
        logTotalUsers(totalUsers.longValue());

        // Adjuntar el JSON completo como attachment en el reporte
        Allure.addAttachment("Dashboard Metrics Response",
                "application/json",
                new ByteArrayInputStream(response.getBody().getBytes()),
                "json");
    }

    @Step("Usuarios totales reportados: {totalUsers}")
    public void logTotalUsers(long totalUsers) {
        // Allure mostrará este valor en los steps
    }

}
