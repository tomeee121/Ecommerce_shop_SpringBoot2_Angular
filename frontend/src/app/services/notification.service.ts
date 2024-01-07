import { Injectable } from '@angular/core';
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../enum/notification-type.enum";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly notifier: NotifierService;

  // @ts-ignore
  constructor(private readonly notifier: NotifierService) {
    this.notifier=notifier;
  }

  public notify(type: NotificationType, message: string){
    this.notifier.notify(type.toString(),message);
  }
}
