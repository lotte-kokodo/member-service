package shop.kokodo.memberservice.vo.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestReview {
    private String loginId;
    private String profileImageUrl;
}
