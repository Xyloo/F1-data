package pl.pollub.f1data.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Services.RaceService;

@RestController
@RequestMapping("api/race")
public class RacingController {

    @Autowired
    RaceService raceService;

    @GetMapping("/{raceId}/best-time")
    public ResponseEntity<String> getBestRaceTime(@PathVariable Integer raceId) {
        String bestTime = raceService.getBestRaceTimeByRaceId(raceId);
        if (bestTime != null) {
            return ResponseEntity.ok(bestTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{raceId}/average-time")
    public ResponseEntity<String> getAverageRaceTime(@PathVariable Integer raceId) {
        String averageTime = raceService.getAverageRaceTime(raceId);
        if (averageTime != null) {
            return ResponseEntity.ok(averageTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{raceId}/pitstops")
    public ResponseEntity<?> getPitstopsCountByLapForRace(@PathVariable Integer raceId) {
        return ResponseEntity.ok(raceService.getPitstopsCountByLapForRace(raceId));
    }


}
