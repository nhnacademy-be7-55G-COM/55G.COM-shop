package shop.s5g.shop.dto.coupon;

import lombok.Getter;
import lombok.Setter;
import shop.s5g.shop.entity.member.Member;

@Getter
public class MemberRegisteredEvent {

    private final Member member;

    public MemberRegisteredEvent(Member member) {
        this.member = member;
    }
}