package kr.gateway.absent;//package kr.gateway.absent;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import java.util.Map;
//
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class NaverMemberInfo implements OAuth2MemberInfo {
//    private Map<String, Object> attributes;
//
//    @Override
//    public String getProviderId() {
//        return (String) ((Map<String, Object>) attributes.get("response")).get("id");
//    }
//
//    @Override
//    public String getProvider() {
//        return "naver";
//    }
//
//    @Override
//    public String getName() {
//        return (String) ((Map<String, Object>) attributes.get("response")).get("name");
//    }
//
//    @Override
//    public String getEmail() {
//        return (String) ((Map<String, Object>) attributes.get("response")).get("email");
//    }
//}
