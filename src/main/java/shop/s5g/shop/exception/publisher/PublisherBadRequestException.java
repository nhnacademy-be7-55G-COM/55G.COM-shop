package shop.s5g.shop.exception.publisher;

import shop.s5g.shop.exception.BadRequestException;

public class PublisherBadRequestException extends BadRequestException {
  public PublisherBadRequestException(String message) {
    super(message);
  }
}
