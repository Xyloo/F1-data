import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {CircuitDetailsModel} from "./circuit-details-model";
import {Chart, ChartConfiguration, registerables} from "chart.js";

@Component({
  selector: 'app-circuit-details',
  templateUrl: './circuit-details.component.html',
  styleUrls: ['./circuit-details.component.css']
})
export class CircuitDetailsComponent {
    circuit: CircuitDetailsModel;
    id: string | null
    raceName: Set<String> = new Set<string>();
    bestLapTime: string = '';
    averageLapTime: string = '';
    bestLapTimeDriver: string = '';
    public chart: any;
    public isHidden: boolean = true;
    constructor(private route: ActivatedRoute, private http: HttpClient) {
        this.id = this.route.snapshot.paramMap.get('id');
    }

    ngOnInit() {
        let avgLapTime: number = 0;
        let avgLapTimeCount: number = 0;
        let bestLapTime: number = 0;
        let bestLapTimeDriver: string = '';
        this.http.get<CircuitDetailsModel>(`/api/circuits/${this.id}`).subscribe(result => {
            this.circuit = result;
            result.races.forEach(race => {
                this.raceName.add(race.name);
                let bestTime = this.convertStringToTime(race.bestLapTime.bestLapTime);
                let avgTime = this.convertStringToTime(race.averageLapTime);
                if (bestLapTime === 0 || bestTime < bestLapTime) {
                    bestLapTime = bestTime;
                    bestLapTimeDriver = race.bestLapTime.forename + ' ' + race.bestLapTime.surname
                }
                avgLapTime += avgTime;
                avgLapTimeCount++;
            })
            this.bestLapTime = this.convertTimeToString(bestLapTime);
            this.averageLapTime = this.convertTimeToString(avgLapTime / avgLapTimeCount);
            this.bestLapTimeDriver = bestLapTimeDriver;
            this.circuit.races.sort((a, b) => a.year - b.year );
        })
    }

    ngAfterViewInit() {
        setTimeout(() => {
            //this.createChart();
        }, 1000);
    }

    convertStringToTime(time: string): number {
        let timeArray = time.split(':');
        let minutes = parseInt(timeArray[0]);
        let remainder = timeArray[1].split('.');
        let seconds = parseInt(remainder[0]);
        let milliseconds = parseInt(remainder[1]);
        return milliseconds + (seconds * 1000) + (minutes * 60 * 1000);
    }

    convertTimeToString(time: number): string {
        let minutes = Math.floor(time / 60000);
        let seconds = Math.floor((time % 60000) / 1000);
        let milliseconds = Math.floor((time % 60000) % 1000);
        return `${minutes}:${seconds}.${milliseconds}`;
    }

    public createChart() {
        Chart.register(...registerables);
        let ctx = <HTMLCanvasElement>document.getElementById('pitstopChart');
        let datasets: object[] = [];
        let maxLaps = 0;
        this.circuit.races.forEach(race => {
            if(race.lapPitstopMap === undefined || race.lapPitstopMap.length === 0) {
                return;
            }
            console.log(race.lapPitstopMap)
            race.lapPitstopMap.forEach((value, key) => {
                if (key > maxLaps) {
                    maxLaps = key;
                }
            })
            datasets.push(race.lapPitstopMap)
        })
        console.log(datasets)
        let cfg = {
            type: 'line',
            data: {
                labels: Array.from(Array(maxLaps).keys()),
                datasets: datasets
            },
            options: {
                parsing: {
                    xAxisKey: 'lap',
                    yAxisKey: 'pitstops'
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        } as ChartConfiguration
        this.chart = new Chart(ctx, cfg);
    }

}
