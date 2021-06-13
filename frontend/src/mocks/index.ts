import {HttpClientTestingModule} from '@angular/common/http/testing';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';

export const MODULE_MOCKS = [
  HttpClientTestingModule,
  LoggerModule.forRoot({ level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.OFF }),
];

export const SERVICE_MOCKS = [
];
