package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Driver entity

 */
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
    @JsonProperty("driverId")
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

    @JsonManagedReference("driver-driverstanding")
    @OneToMany(mappedBy = "driver")
    private Set<Driverstanding> driverstandings = new LinkedHashSet<>();

    @JsonManagedReference("driver-laptime")
    @OneToMany(mappedBy = "driver")
    private Set<Laptime> laptimes = new LinkedHashSet<>();

    @JsonManagedReference("driver-pitstop")
    @OneToMany(mappedBy = "driver")
    private Set<Pitstop> pitstops = new LinkedHashSet<>();

    @JsonManagedReference("driver-qualifying")
    @OneToMany(mappedBy = "driver")
    private Set<Qualifying> qualifyings = new LinkedHashSet<>();

    @JsonManagedReference("driver-result")
    @OneToMany(mappedBy = "driver")
    private Set<Result> results = new LinkedHashSet<>();

    @JsonManagedReference("driver-sprintresult")
    @OneToMany(mappedBy = "driver")
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}