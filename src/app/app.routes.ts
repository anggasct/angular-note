import { Routes } from '@angular/router';
import { NoteEditorComponent } from './components/note-editor/note-editor.component';

export const routes: Routes = [
  { path: 'notes/:id', component: NoteEditorComponent },
  { path: 'notes', component: NoteEditorComponent },
  { path: '', redirectTo: '/notes', pathMatch: 'full' },
];
