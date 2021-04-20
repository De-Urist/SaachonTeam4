package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.ChiefDoctor;

@Data
@NoArgsConstructor
public class ChiefDoctorRepresentation {

    private int id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String uri;

    public ChiefDoctorRepresentation(ChiefDoctor cdoctor) {
        if (cdoctor != null) {
            id = cdoctor.getId();
            name = cdoctor.getName();
            username = cdoctor.getUsername();
            password = cdoctor.getPassword();
            role = cdoctor.getRole();
            uri = "http://localhost:9000/v1/chief/" + cdoctor.getId();
        }
    }

    public ChiefDoctor createChiefDoctor() {
        ChiefDoctor cdoctor = new ChiefDoctor();
        cdoctor.setName(name);
        cdoctor.setUsername(username);
        cdoctor.setPassword(password);
        cdoctor.setRole(role);
        return cdoctor;
    }
}
