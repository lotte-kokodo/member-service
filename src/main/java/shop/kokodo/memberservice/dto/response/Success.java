package shop.kokodo.memberservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Success<T> implements Result {
    private T data;
}