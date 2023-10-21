package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "drivers", schema = "f1datadb", indexes = {
        @Index(name = "url", columnList = "url", unique = true)
})
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverId", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "driverRef", nullable = false)
    private String driverRef;

    @Column(name = "number")
    private Integer number;

    @Size(max = 3)
    @Column(name = "code", length = 3)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "forename", nullable = false)
    private String forename;

    @Size(max = 255)
    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 255)
    @Column(name = "nationality")
    private String nationality;

    @Size(max = 255)
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Driverstanding> driverstandings = new LinkedHashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Laptime> laptimes = new LinkedHashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Pitstop> pitstops = new LinkedHashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Qualifying> qualifyings = new LinkedHashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Result> results = new LinkedHashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "driver")
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}