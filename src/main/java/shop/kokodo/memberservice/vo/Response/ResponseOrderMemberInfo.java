package shop.kokodo.memberservice.vo.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.kokodo.memberservice.dto.MemberDto;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrderMemberInfo {

    private String name;
    private String email;
    private String phoneNumber;

    private String address;

    @Builder
    public ResponseOrderMemberInfo(String name, String email, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.address = address;
    }
}