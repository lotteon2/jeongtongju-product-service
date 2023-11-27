package com.jeontongju.product.repository;

import com.jeontongju.product.domain.Shorts;
import com.jeontongju.product.enums.ShortsTypeEnum;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ShortsRepositoryTest {

  @Autowired private ShortsRepository shortsRepository;

  @Test
  @DisplayName("TEST - create shorts")
  void createShorts() {

    Shorts shortsEntity =
        Shorts.builder()
            .sellerId(1L)
            .productId(UUID.randomUUID())
            .video("/video/example")
            .thumbnail("/thumbnail/example")
            .title("복순이 복순복순 싸게 팔아요")
            .description("엄청 싸요!!!")
            .type(ShortsTypeEnum.PRODUCT)
            .build();
    Shorts shorts = shortsRepository.save(shortsEntity);
    Assertions.assertThat(shorts.getShortsId()).isNotNull();
    Assertions.assertThat(shorts.getProductId()).isSameAs(shortsEntity.getProductId());
    Assertions.assertThat(shorts.getType()).isSameAs(ShortsTypeEnum.PRODUCT);
  }
}
