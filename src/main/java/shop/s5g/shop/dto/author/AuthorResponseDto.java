package shop.s5g.shop.dto.author;

import shop.s5g.shop.dto.profile.ProfileResponseDto;

public record AuthorResponseDto(
    long authorId,
    ProfileResponseDto  profile,
    String name,
    boolean active
) {

}
