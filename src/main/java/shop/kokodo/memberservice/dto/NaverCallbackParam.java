package shop.kokodo.memberservice.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NaverCallbackParam {

    private String code;
    private String state;
    private String error;
    private String error_description;

}
