package rex.chien.cms.domain;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Data
public class ClientRequest {
    private long id;

    private String name;

    private String email;

    private String phone;

    private Long company;
}
