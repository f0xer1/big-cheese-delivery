package com.bcd.big_cheese_delivery.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.repository.MongoRepository;

@UtilityClass
public class MongoRepositoryUtils {
  public static <T, ID> T getById(ID id, MongoRepository<T, ID> mongoRepository) {
    return mongoRepository.findById(id).orElseThrow(AssertionError::new);
  }
}
