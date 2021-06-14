import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TestBed, waitForAsync} from '@angular/core/testing';
import {KeycloakAngularModule} from 'keycloak-angular';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {ButtonModule} from 'primeng/button';
import {AppComponent} from './app.component';
import {SERVICE_MOCKS} from "../mocks";

describe('AppComponent', () => {

  let fixture, component;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        KeycloakAngularModule,
        ButtonModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.OFF}),
      ],
      declarations: [
        AppComponent
      ],
      providers: [
          SERVICE_MOCKS
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.debugElement.componentInstance;
  }));

  it('should create the app', waitForAsync(() => {
    expect(component).toBeTruthy();
  }));

  it('should render title in a h1 tag', waitForAsync(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('To-Do List');
  }));
});
