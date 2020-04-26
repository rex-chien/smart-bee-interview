package rex.chien.cms.domain;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    private String name;

    private String email;

    private String phone;

    private Long createdBy;

    private Date createdAt;

    private Long updatedBy;

    private Date updatedAt;
}
