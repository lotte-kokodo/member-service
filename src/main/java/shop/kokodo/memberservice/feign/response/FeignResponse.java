package shop.kokodo.memberservice.feign.response;

public class FeignResponse {

    public interface MemberDeliveryInfo {

        String getName();
        String getAddress();

    }
}
