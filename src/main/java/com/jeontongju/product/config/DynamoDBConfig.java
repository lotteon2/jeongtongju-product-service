package com.jeontongju.product.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeontongju.product.dynamodb.domian.ProductProductRecordAdditionalContents;
import com.jeontongju.product.dynamodb.domian.ProductRecordContents;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.jeontongju.product.dynamodb"})
public class DynamoDBConfig {

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;

  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(accessKey, secretKey);
  }

  public AWSCredentialsProvider amazonAWSCredentialsProvider() {
    return new AWSStaticCredentialsProvider(amazonAWSCredentials());
  }

  @Bean
  @Primary
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Bean
  @Primary
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(amazonAWSCredentialsProvider())
        .withRegion(Regions.AP_NORTHEAST_2)
        .build();
  }

  @Bean
  @Primary
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    return new DynamoDBMapper(amazonDynamoDB, config);
  }

  public static class LocalDateTimeConverter implements DynamoDBTypeConverter<Date, LocalDateTime> {
    @Override
    public Date convert(LocalDateTime source) {
      return Date.from(source.toInstant(ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime unconvert(Date source) {
      return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime();
    }
  }

  public static class ProductRecodeConverter
      implements DynamoDBTypeConverter<String, ProductRecordContents> {

    @Override
    public String convert(ProductRecordContents productRecodeContents) {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonStr = null;

      try {
        jsonStr = objectMapper.writeValueAsString(productRecodeContents);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage());
      }
      return jsonStr;
    }

    @Override
    public ProductRecordContents unconvert(String s) {

      ObjectMapper objectMapper = new ObjectMapper();
      ProductRecordContents productRecodeContents = new ProductRecordContents();
      try {
        productRecodeContents = objectMapper.readValue(s, ProductRecordContents.class);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      return productRecodeContents;
    }
  }

  public static class ProductRecodeAdditionalConverter
      implements DynamoDBTypeConverter<String, ProductProductRecordAdditionalContents> {

    @Override
    public String convert(ProductProductRecordAdditionalContents productRecodeAdditionalContents) {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonStr = null;

      try {
        jsonStr = objectMapper.writeValueAsString(productRecodeAdditionalContents);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage());
      }
      return jsonStr;
    }

    @Override
    public ProductProductRecordAdditionalContents unconvert(String s) {

      ObjectMapper objectMapper = new ObjectMapper();
      ProductProductRecordAdditionalContents productRecodeAdditionalContents =
          new ProductProductRecordAdditionalContents();
      try {
        productRecodeAdditionalContents =
            objectMapper.readValue(s, ProductProductRecordAdditionalContents.class);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      return productRecodeAdditionalContents;
    }
  }
}
