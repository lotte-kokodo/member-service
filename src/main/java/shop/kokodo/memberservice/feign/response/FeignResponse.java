package shop.kokodo.memberservice.feign.response;

public class FeignResponse {

    public interface MemberAddress {

        String getAddress();

    }

    public interface MemberOfOrderSheet {

        String getName();
        String getPhoneNumber();
        String getEmail();
        String getAddress();

    }

}
