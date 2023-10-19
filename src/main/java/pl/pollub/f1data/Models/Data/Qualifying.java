package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "qualifying", schema = "f1datadb")
public class Qualifying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qualifyId", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    private Race race;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driverId", nullable = false)
    private Driver driver;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "constructorId", nullable = false)
    private Constructor constructor;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "position")
    private Integer position;

    @Size(max = 255)
    @Column(name = "q1")
    private String q1;

    @Size(max = 255)
    @Column(name = "q2")
    private String q2;

    @Size(max = 255)
    @Column(name = "q3")
    private String q3;

}