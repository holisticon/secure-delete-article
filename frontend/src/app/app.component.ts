import {Component, OnInit} from "@angular/core";
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";
import {NGXLogger} from "ngx-logger";
import {User, UserService} from "../../src-gen";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  user: User;

  constructor(
    private readonly keycloak: KeycloakService,
    private logger: NGXLogger,
    private userService: UserService
  ) {}

  printApplications(): void {

    this.userService.createUser('12345', {
      userId: '12345',
      name: 'hans.wurst',
    }).subscribe(user => {
      this.user = user;
    });
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
