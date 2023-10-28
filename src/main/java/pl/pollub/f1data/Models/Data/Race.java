package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "races", schema = "f1datadb", indexes = {
        @Index(name = "url", columnList = "url", unique = true)
})
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raceId", nullable = false)
    @JsonProperty("raceId")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "year", nullable = false)
    @JsonBackReference(value = "race-year")
    private Season year;

    @NotNull
    @Column(name = "round", nullable = false)
    private Integer round;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "circuitId", nullable = false)
    @JsonBackReference(value = "circuit-race")
    private Circuit circuit;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

    @Column(name = "fp1_date")
    private LocalDate fp1Date;

    @Column(name = "fp1_time")
    private LocalTime fp1Time;

    @Column(name = "fp2_date")
    private LocalDate fp2Date;

    @Column(name = "fp2_time")
    private LocalTime fp2Time;

    @Column(name = "fp3_date")
    private LocalDate fp3Date;

    @Column(name = "fp3_time")
    private LocalTime fp3Time;

    @Column(name = "quali_date")
    private LocalDate qualiDate;

    @Column(name = "quali_time")
    private LocalTime qualiTime;

    @Column(name = "sprint_date")
    private LocalDate sprintDate;

    @Column(name = "sprint_time")
    private LocalTime sprintTime;

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-constructorresult")
    private Set<Constructorresult> constructorresults = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-constructorstanding")
    private Set<Constructorstanding> constructorstandings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-driverstanding")
    private Set<Driverstanding> driverstandings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-laptime")
    private Set<Laptime> laptimes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-pitstop")
    private Set<Pitstop> pitstops = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-qualifying")
    private Set<Qualifying> qualifyings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-result")
    private Set<Result> results = new LinkedHashSet<>();

    @OneToMany(mappedBy = "race")
    @JsonManagedReference(value = "race-sprintresult")
    private Set<Sprintresult> sprintresults = new LinkedHashSet<>();

}