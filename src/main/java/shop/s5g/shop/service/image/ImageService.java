package shop.s5g.shop.service.image;

import shop.s5g.shop.dto.image.ImageResponseDto;

public interface ImageService {

    ImageResponseDto uploadImage(String path, byte[] image);

}
