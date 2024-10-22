package shop.S5G.shop.dto;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {
//    private Long publisherId;
    private String publisherName;
    private boolean active;
}
