import {InjectionToken} from "@angular/core";

export interface Configuration {
  extension: string;
  popupOpened: boolean;
  targetElementSelector: string;
  variableDeclaration: 'CONST' | 'VAR' | 'LET';
  variableNameStrategy: 'TYPE_BASED' | 'RANDOM';
}

export const CONFIGURATION = new InjectionToken<Configuration>('jsgenerator.configuration');
