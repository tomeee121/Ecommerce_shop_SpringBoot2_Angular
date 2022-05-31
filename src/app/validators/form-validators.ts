import {FormControl, ValidationErrors} from "@angular/forms";

export class FormValidators {

  static rejectOnlyWhiteSpace(control: FormControl): ValidationErrors{
    if(control != null && control.value.trim().length === 0){
      return {'rejectOnlyWhiteSpace': true};
    }
    else {
      return {'rejectOnlyWhiteSpace': false};
    }

  }
}
