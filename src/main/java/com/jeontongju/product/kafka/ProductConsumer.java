package com.jeontongju.product.kafka;

import com.jeontongju.product.service.ProductService;
import io.github.bitbox.bitbox.dto.*;
import io.github.bitbox.bitbox.enums.NotificationTypeEnum;
import io.github.bitbox.bitbox.enums.RecipientTypeEnum;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {

  private final ProductService productService;
  private final ProductProducer productProducer;

  @KafkaListener(topics = KafkaTopicNameInfo.DELETE_SELLER_TO_PRODUCT)
  public void deleteProductFromSeller(Long sellerId) {
    productService.deleteProductByDeleteSeller(sellerId);
  }

  @KafkaListener(topics = KafkaTopicNameInfo.UPDATE_SELLER_TO_PRODUCT)
  public void updateProductFromSeller(SellerInfoDto sellerInfoDto) {
    productService.modifyProductByModifySeller(sellerInfoDto);
  }

  @KafkaListener(topics = KafkaTopicNameInfo.REDUCE_STOCK)
  public void reduceStockFromOrder(OrderInfoDto orderInfoDto) {

    try {
      // 재고 차감
      productService.reduceStock(orderInfoDto.getProductUpdateDto());
      log.info(orderInfoDto.getProductUpdateDto().toString());

      // order-service 에 create-order 보내기
      productProducer.createOrderToOrder(orderInfoDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      sendOrderInfoDto(orderInfoDto); // 롤백

      productProducer.sendErrorNotification(
          ServerErrorForNotificationDto.builder()
              .notificationType(NotificationTypeEnum.INTERNAL_PRODUCT_SERVER_ERROR)
              .recipientId(orderInfoDto.getOrderCreationDto().getConsumerId())
              .recipientType(RecipientTypeEnum.ROLE_CONSUMER)
              .createdAt(LocalDateTime.now())
              .error(orderInfoDto)
              .build());
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.ADD_STOCK)
  public void addStockFromOrder(OrderInfoDto orderInfoDto) { // 주문에서 터져서 롤백
    // 재고 되돌리기
    try {
      productService.rollbackStock(orderInfoDto.getProductUpdateDto());
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      sendOrderInfoDto(orderInfoDto);
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.UPDATE_PRODUCT_SALES_COUNT)
  public void updateProductSalesCountFromOrder(ProductUpdateListDto productUpdateDtoList) {
    log.info(
        "상품"
            + productUpdateDtoList.getProductUpdateDtoList().get(0).getProductId().toString()
            + "----"
            + productUpdateDtoList.getProductUpdateDtoList().get(0).getProductCount().toString());

    // 재고 확인 후, 5개 미만 알림
    productService.checkProductStock(productUpdateDtoList.getProductUpdateDtoList());

    // 주문으로 판매량 증가
    productService.addProductMetricsFromOrder(productUpdateDtoList.getProductUpdateDtoList());
  }

  // 주문 취소 로직
  @KafkaListener(topics = KafkaTopicNameInfo.CANCEL_ORDER_STOCK)
  public void addStockFromCancelOrder(OrderCancelDto orderCancelDto) {
    try {
      log.info(
          "상품"
              + orderCancelDto.getProductUpdateDtoList().get(0).getProductId().toString()
              + orderCancelDto.getProductUpdateDtoList().get(0).getProductCount().toString());
      // 주문 취소로 재고 복구
      productService.rollbackStock(orderCancelDto.getProductUpdateDtoList());
      // 결제 서버로 카프카
      productProducer.cancelOrderPayment(orderCancelDto);
      // 주문 취소로 판매량 감소
      productService.reduceProductMetricsFromCancelOrder(orderCancelDto.getProductUpdateDtoList());

    } catch (Exception e) {
      log.error(e.getMessage());
      sendOrderCancel(orderCancelDto); // 롤백 셋 중 하나로
      // 재고에서 터져서 알림
      productProducer.sendNotificationByOrderCancelFail(
          MemberInfoForNotificationDto.builder()
              .recipientId(orderCancelDto.getConsumerId())
              .recipientType(RecipientTypeEnum.ROLE_CONSUMER)
              .notificationType(NotificationTypeEnum.INTERNAL_PRODUCT_SERVER_ERROR)
              .createdAt(LocalDateTime.now())
              .build());
    }
  }

  // 결제 서버애서 터져서 주문 취소 롤백
  @KafkaListener(topics = KafkaTopicNameInfo.RECOVER_CANCEL_ORDER_STOCK)
  public void recoverFromCancelOrder(OrderCancelDto orderCancelDto) {
    try {
      log.info(
          "상품"
              + orderCancelDto.getProductUpdateDtoList().get(0).getProductId().toString()
              + "----"
              + orderCancelDto.getProductUpdateDtoList().get(0).getProductCount().toString());
      // 주문 취소의 실패로 재고 다시 차감
      productService.reduceStock(orderCancelDto.getProductUpdateDtoList());
      // 주문 취소의 실패로 판매량 다시 증가
      productService.addProductMetricsFromOrder(orderCancelDto.getProductUpdateDtoList());
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      sendOrderCancel(orderCancelDto); // 롤백 셋 중 하나로
    }
  }

  public void sendOrderInfoDto(OrderInfoDto orderInfoDto) {
    if (orderInfoDto.getUserCouponUpdateDto().getCouponCode() != null) {
      // 쿠폰
      productProducer.rollbackCouponByCancel(orderInfoDto);
    } else if (orderInfoDto.getUserPointUpdateDto().getPoint() != null) {
      // 포인트
      productProducer.addPointByCancel(orderInfoDto);
    }
  }

  // 주문 취소의 실패로 셋 중 하나
  public void sendOrderCancel(OrderCancelDto orderCancelDto) {
    if (orderCancelDto.getCouponCode() != null) {
      // 쿠폰
      productProducer.recoverCancelOrderCoupon(orderCancelDto);
    } else if (orderCancelDto.getPoint() != null) {
      // 포인트
      productProducer.recoverCancelOrderPoint(orderCancelDto);
    } else {
      productProducer.recoverCancelOrder(orderCancelDto);
    }
  }
}
