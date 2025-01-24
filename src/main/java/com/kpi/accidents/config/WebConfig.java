package com.kpi.accidents.config;

import com.kpi.accidents.repository.AccidentData;
import com.kpi.accidents.service.AccidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kpi.accidents.controller")
public class WebConfig {
    private final AccidentData accidentData;

    @Autowired
    public WebConfig(AccidentData accidentData) {
        this.accidentData = accidentData;
    }

    @Bean
    public AccidentData getAccidentData() {
        return accidentData;
    }

    @Bean
    public AccidentService getAccidentService() {
        return new AccidentService(getAccidentData());
    }
}
