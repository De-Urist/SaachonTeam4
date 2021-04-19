package team4.Sacchon.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cfg.CreateKeySecondPass;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
