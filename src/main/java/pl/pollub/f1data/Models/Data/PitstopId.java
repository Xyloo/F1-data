package pl.pollub.f1data.Models.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PitstopId implements Serializable {
    private static final long serialVersionUID = -3315394335165689304L;
    @NotNull
    @Column(name = "raceId", nullable = false)
    private Integer raceId;

    @NotNull
    @Column(name = "driverId", nullable = false)
    private Integer driverId;

    @NotNull
    @Column(name = "stop", nullable = false)
    private Integer stop;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PitstopId entity = (PitstopId) o;
        return Objects.equals(this.raceId, entity.raceId) &&
                Objects.equals(this.driverId, entity.driverId) &&
                Objects.equals(this.stop, entity.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId, driverId, stop);
    }

}