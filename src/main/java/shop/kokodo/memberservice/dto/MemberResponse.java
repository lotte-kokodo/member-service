package shop.kokodo.memberservice.dto;

public class MemberResponse {

    public interface MemberDeliveryInfo {

        String getAddress();
        String getName();

    }

    public interface MemberOfOrderSheet {

        String getName();
        String getPhoneNumber();
        String getEmail();
        String getAddress();

    }
}
