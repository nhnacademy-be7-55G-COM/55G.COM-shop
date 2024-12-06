package shop.s5g.shop.dto.member_status;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberStatusRequestDto(
    @NotNull
    @NotEmpty
    @Length(max = 15)
    String typeName
) {

}
