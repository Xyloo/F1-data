package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Status entity (essentially an enum)
 */
@Getter
@Setter
@Entity
@Table(name = "status", schema = "f1datadb")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId", nullable = false)
    @JsonProperty("statusId")
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "status")
    @JsonManagedReference(value = "status-result")
    private Set<Result> results = new LinkedHashSet<>();

    @OneToMany(mappedBy = "status")
    @JsonManagedReference(value = "status-sprintresult")
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}