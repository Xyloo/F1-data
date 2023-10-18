package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "constructorstandings")
public class Constructorstanding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constructorStandingsId", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    private Race race;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "constructorId", nullable = false)
    private Constructor constructor;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @Column(name = "position")
    private Integer position;

    @Size(max = 255)
    @Column(name = "positionText")
    private String positionText;

    @NotNull
    @Column(name = "wins", nullable = false)
    private Integer wins;

}