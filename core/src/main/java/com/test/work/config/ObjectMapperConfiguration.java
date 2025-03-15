/*
 *  =======================================================================
 *
 *  Copyright (c) 2020 Northern Capital Gateway, LLC. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Northern Capital Gateway, LLC.
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with
 *  Northern Capital Gateway, LLC
 *
 *  =======================================================================
 */

package com.test.work.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@AllArgsConstructor
public class ObjectMapperConfiguration {
    @Bean("snakeCaseObjectMapper")
    public ObjectMapper snakeCaseObjectMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        result.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        result.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        result.registerModule(new Jdk8Module());
        result.registerModule(new JavaTimeModule());
        result.registerModule(new ParameterNamesModule());

        var moduleLocalDateTime = new SimpleModule();
        result.registerModule(moduleLocalDateTime);

        return result;
    }

    @Bean("lowerCaseObjectMapper")
    public ObjectMapper lowerCaseObjectMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        result.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        result.registerModule(new Jdk8Module());
        result.registerModule(new JavaTimeModule());
        result.registerModule(new ParameterNamesModule());

        var moduleLocalDateTime = new SimpleModule();
        result.registerModule(moduleLocalDateTime);

        return result;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JsonNullableModule());
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
