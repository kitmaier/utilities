import { CoursesService } from './courses.service';
import { Component } from '@angular/core';

@Component({
    selector: 'courses',
    template: `
        <h2>{{title}}</h2>
        <h3>{{courses.length+" Courses"}}</h3>
        <ul>
            <li *ngFor="let course of courses">
                {{course}}
            </li>
        </ul>
    `
})
export class CoursesComponent {
    title = "List of courses";
    courses : string[];
    constructor(coursesService: CoursesService) {
    //constructor() {
        //let coursesService = new CoursesService();
        this.courses = coursesService.getCourses();
    }
}