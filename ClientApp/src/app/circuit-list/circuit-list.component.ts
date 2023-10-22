import { Component } from '@angular/core';
import {CircuitModel} from "./circuit-model";
import {HttpClient} from "@angular/common/http";
import {CircuitDeserializedModel} from "./circuit-deserialized-model";

@Component({
  selector: 'app-circuit-list',
  templateUrl: './circuit-list.component.html',
  styleUrls: ['./circuit-list.component.css'],
})
export class CircuitListComponent {

  circuits : CircuitModel[] = [];
  sortedByNameAsc = false;
    sortedByCountryAsc = false;
    constructor(private http: HttpClient) { }

    ngOnInit(): void {
      this.http.get<CircuitDeserializedModel[]>('/api/circuits/').subscribe(result => {
        result.map(circuit => {
          this.circuits.push(new CircuitModel(circuit.id, circuit.name, circuit.location, circuit.country, circuit.url, circuit.lat, circuit.lng));
          this.circuits.sort((a, b) => a.country.localeCompare(b.country));
        })
      })
    }

    public sortByName() {
        if(!this.sortedByNameAsc) {
            this.circuits.sort((a, b) => a.name.localeCompare(b.name));
            this.sortedByNameAsc = true;
        } else {
            this.circuits.sort((a, b) => b.name.localeCompare(a.name));
            this.sortedByNameAsc = false;
        }
    }

    public sortByCountry() {
        if(!this.sortedByCountryAsc) {
        this.circuits.sort((a, b) => a.country.localeCompare(b.country));
        this.sortedByCountryAsc = true;
        } else {
            this.circuits.sort((a, b) => b.country.localeCompare(a.country));
            this.sortedByCountryAsc = false;
        }
    }
}
