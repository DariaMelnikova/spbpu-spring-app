import { Injectable } from '@angular/core';
import { 
  Router,
  CanActivate,
  ActivatedRouteSnapshot
} from '@angular/router';
import { AppService } from './app.service';

@Injectable()
export class RoleGuardService implements CanActivate {

  constructor(public auth: AppService, public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRole = route.data.expectedRole;

    const userRole = localStorage.getItem('occupation');

    if (!this.auth.isAuthenticated() || userRole !== expectedRole) {
        window.alert("only " + expectedRole + " can access this")
      this.router.navigate(['login']);

      return false;
    }

    return true;
  }

}