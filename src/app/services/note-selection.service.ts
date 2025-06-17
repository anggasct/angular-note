import { Injectable, signal } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NoteSelectionService {
  selectedNoteId = signal<number | null | undefined>(null);
  notesChanged = new Subject<void>();

  selectNote(id: number | null | undefined) {
    this.selectedNoteId.set(id);
  }

  notifyNotesChanged() {
    this.notesChanged.next();
  }
}
