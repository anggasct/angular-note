import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  imports: [
    MatSidenavModule,
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
  host: {
    class: 'h-100 d-flex flex-column'
  }
})
export class App {
  protected title = 'angular-note';
}
