import {Component, OnInit} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConstructorDeserializedModel} from './constructor-deserialized-model';
@Component({
  selector: 'app-constructors',
  templateUrl: './constructors.component.html',
  styleUrls: ['./constructors.component.css']
})
export class ConstructorsComponent implements OnInit {

    constructorData: ConstructorDeserializedModel[] = [];
    readonly endpoint = '/api/constructor/';

    constructor(private http: HttpClient) { }

    ngOnInit(): void {
        this.fetchData();
    }

    fetchData(): void {
        this.http.get<ConstructorDeserializedModel[]>(this.endpoint)
            .subscribe(result =>
                result.map(constructor => {
                    this.constructorData.push(new ConstructorDeserializedModel(constructor.id,
                        constructor.constructorRef, constructor.name, constructor.nationality,
                        constructor.url));
                    this.constructorData.sort((a, b) => a.constructorRef.localeCompare(b.constructorRef));
        }));
    }
}
