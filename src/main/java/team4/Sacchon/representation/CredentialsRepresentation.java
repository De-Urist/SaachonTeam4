//package team4.Sacchon.representation;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import team4.Sacchon.model.ChiefDoctor;
//import team4.Sacchon.model.Credentials;
//
//@Data
//@NoArgsConstructor
//public class CredentialsRepresentation {
//
//    private int id;
//    private String username;
//    private String password;
//    private String uri;
//
//    public CredentialsRepresentation(Credentials credentials) {
//        if (credentials != null) {
//            username = credentials.getUsername();
//            password = credentials.getPassword();
//            uri = "http://localhost:9000/v1/chief/" + cdoctor.getId();
//        }
//    }
//
//    public ChiefDoctor createChiefDoctor() {
//        ChiefDoctor cdoctor = new ChiefDoctor();
//        cdoctor.setName(name);
//        cdoctor.setUsername(username);
//        cdoctor.setPassword(password);
//        cdoctor.setRole(role);
//        return cdoctor;
//    }
//}
