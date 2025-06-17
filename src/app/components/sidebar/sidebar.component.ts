import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Note } from '../../models/note.model';
import { NoteSelectionService } from '../../services/note-selection.service';
import { NoteService } from '../../services/note.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatListModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSnackBarModule
  ],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent implements OnInit, OnDestroy {
  notes = signal<Note[]>([]);

  private readonly noteService = inject(NoteService);
  private readonly noteSelectionService = inject(NoteSelectionService);
  private readonly router = inject(Router);
  private readonly destroy$ = new Subject<void>();

  ngOnInit() {
    this.loadNotes();

    this.noteSelectionService.notesChanged
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.loadNotes();
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadNotes() {
    this.noteService.getNotes().subscribe({
      next: (response) => {
        this.notes.set(response.content);
        if (response.content.length > 0 && !this.noteSelectionService.selectedNoteId() &&
          this.router.url === '/') {
          this.selectNote(response.content[0].id!);
        }
      },
      error: (error) => console.error('Error loading notes', error)
    });
  }

  selectNote(id: number) {
    this.noteSelectionService.selectNote(id);
    this.router.navigate(['/notes', id]);
  }

  createNewEmptyNote() {
    this.noteSelectionService.selectNote(null);
    this.router.navigate(['/notes/new']);
  }

  getSelectedNoteId(): number | null | undefined {
    return this.noteSelectionService.selectedNoteId();
  }
}
