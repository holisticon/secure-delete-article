import {Component, OnInit} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";
import {NGXLogger} from "ngx-logger";
import {ToDoItem, TodoService, User, UserService} from "../../src-gen";
import {flatMap, tap} from "rxjs/operators";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  user: User;
  toDoItems: ToDoItem[];

  constructor(
      private readonly keycloak: KeycloakService,
      private logger: NGXLogger,
      private userService: UserService,
      private todoService: TodoService
  ) {}

  printApplications(): void {

    this.userService.createUser('12345', {
      userId: '12345',
      name: 'hans.wurst',
    })
        .pipe(
            tap(user => this.user = user),
            flatMap(() => this.todoService.addEntry(this.user.userId, {title: 'item 1', description: 'the first item'})),
            flatMap(() => this.todoService.addEntry(this.user.userId, {title: 'item 2', description: 'the second item'})),
            flatMap(() => this.todoService.getToDoList(this.user.userId))
        )
        .subscribe(
            toDoItems => this.toDoItems = toDoItems
        );
  }

  public async ngOnInit() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.printApplications();
    }
  }

  public login() {
    this.keycloak.login();
  }

  public logout() {
    this.keycloak.logout();
  }
}
