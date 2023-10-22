package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Services.RaceService;

@RestController
@RequestMapping("api/racing")
public class RacingController {

    @Autowired
    RaceService raceService;

    @GetMapping("/{raceId}/best-time")
    public ResponseEntity<DriverBestTimeDto> getBestRaceTimeByRaceId(@PathVariable Integer raceId) {
        DriverBestTimeDto bestTime = raceService.getBestRaceTimeByRaceId(raceId).orElse(null);
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
