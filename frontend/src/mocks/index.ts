import {HttpClientTestingModule} from '@angular/common/http/testing';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {TodoService, UserService} from "../../src-gen";
import {TodoServiceMock} from "./todo.service.mock";
import {UserServiceMock} from "./user.service.mock";

export const MODULE_MOCKS = [
  HttpClientTestingModule,
  LoggerModule.forRoot({ level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.OFF }),
];

export const SERVICE_MOCKS = [
  {
    provide: UserService,
    useClass: UserServiceMock
  },
  {provide: TodoService,
  useClass: TodoServiceMock}
];
