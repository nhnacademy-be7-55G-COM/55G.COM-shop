package shop.S5G.shop.exception.publisher;

import shop.S5G.shop.exception.BadRequestException;

public class PublisherBadRequestException extends BadRequestException {
  public PublisherBadRequestException(String message) {
    super(message);
  }
}
