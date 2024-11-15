package shop.s5g.shop.dto.profile;

public record ProfileResponseDto(
    long profileId,
    String birth,
    int debutYear,
    String introduction,
    String imageName,
    boolean active
) {

}
