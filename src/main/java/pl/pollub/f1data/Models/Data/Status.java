package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "status")
    private Set<Result> results = new LinkedHashSet<>();

    @OneToMany(mappedBy = "status")
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}