package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "constructors", schema = "f1datadb", indexes = {
        @Index(name = "name", columnList = "name", unique = true)
})
public class Constructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constructorId", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "constructorRef", nullable = false)
    private String constructorRef;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "nationality")
    private String nationality;

    @Size(max = 255)
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "constructor")
    @JsonBackReference
    private Set<Constructorresult> constructorresults = new LinkedHashSet<>();

    @OneToMany(mappedBy = "constructor")
    @JsonBackReference
    private Set<Constructorstanding> constructorstandings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "constructor")
    @JsonBackReference
    private Set<Qualifying> qualifyings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "constructor")
    @JsonBackReference
    private Set<Result> results = new LinkedHashSet<>();

    @OneToMany(mappedBy = "constructor")
    @JsonBackReference
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}