import { Component, AfterViewInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";
import { Chart, ChartConfiguration, registerables } from "chart.js";
Chart.register(...registerables);

@Component({
    selector: 'app-constructor-details',
    templateUrl: './constructor-details.component.html',
    styleUrls: ['./constructor-details.component.css']
})

export class ConstructorDetailsComponent implements AfterViewInit {
    id: string | null;
    public constructorChart!: Chart;
    public isData: boolean = false;
    constructorName: string = '';

    constructor(private route: ActivatedRoute, private http: HttpClient) {
        this.id = this.route.snapshot.paramMap.get('id');
    }

    ngOnInit(): void {
        this.http.get<{ name: string }>(`/api/constructor/${this.id}`).subscribe(data => {
            this.constructorName = data.name;
        })
    }
    ngAfterViewInit(): void {
        this.fetchDataAndCreateChart();
    }

    fetchDataAndCreateChart() {
        this.http.get<{ year: number, constructor_name: string, total_points: number }[]>(`/api/constructor/results/${this.id}`)
            .subscribe(data => {
                if (Array.isArray(data) && data.length > 0) {
                    this.isData = true;
                    data.sort((a, b) => a.year - b.year);

                    this.createChart(data);
                } else {
                    this.isData = false;
                }
            });
    }

    createChart(data: { year: number, constructor_name: string, total_points: number }[]) {
        const chartData = {
            labels: data.map(item => item.year.toString()),
            datasets: [{
                label: `Total Points`,
                data: data.map(item => item.total_points),
                borderColor: 'blue',
                backgroundColor: 'rgba(135, 206, 235, 0.2)', // light blue with some transparency
                fill: true
            }]
        };

        if (this.constructorChart) {
            this.constructorChart.destroy();
        }

        const chartConfig: ChartConfiguration = {
            type: 'line',
            data: chartData,
            options: {
                layout: {
                    padding: 30
                },
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        };

        const chartCanvas = <HTMLCanvasElement>document.getElementById('constructorChart');
        const ctx = chartCanvas.getContext('2d');
        this.constructorChart = new Chart(ctx!, chartConfig);
    }
}
