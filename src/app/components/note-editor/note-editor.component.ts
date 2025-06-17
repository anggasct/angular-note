import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { QuillModule } from 'ngx-quill';
import { Subject, takeUntil } from 'rxjs';
import { Note } from '../../models/note.model';
import { NoteSelectionService } from '../../services/note-selection.service';
import { NoteService } from '../../services/note.service';

@Component({
  selector: 'app-note-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatDividerModule,
    QuillModule
  ],
  templateUrl: './note-editor.component.html',
})
export class NoteEditorComponent implements OnInit, OnDestroy {
  currentNote = signal<Note | null>(null);
  isSaving = signal<boolean>(false);
  isNewNote = signal<boolean>(false);

  quillConfig = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ 'header': 1 }, { 'header': 2 }],
      [{ 'list': 'ordered' }, { 'list': 'bullet' }],
      [{ 'indent': '-1' }, { 'indent': '+1' }],
      [{ 'direction': 'rtl' }],
      [{ 'size': ['small', false, 'large', 'huge'] }],
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'font': [] }],
      [{ 'align': [] }],
      ['clean'],
      ['link']
    ]
  };

  private readonly noteService = inject(NoteService);
  private readonly noteSelectionService = inject(NoteSelectionService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  private readonly destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.setupRouteListening();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  createNewNote(): void {
    this.router.navigate(['/notes/new']);
  }

  saveNote(): void {
    if (!this.currentNote()) return;

    this.isSaving.set(true);

    if (this.isNewNote()) {
      this.createNoteRequest();
    } else {
      this.updateNoteRequest();
    }
  }

  deleteNote(): void {
    const note = this.currentNote();
    if (!note?.id) return;

    if (confirm('Are you sure you want to delete this note?')) {
      this.noteService.deleteNote(note.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.handleDeleteSuccess();
          },
          error: (error) => {
            this.handleOperationError('Error deleting note', error);
          }
        });
    }
  }

  private setupRouteListening(): void {
    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        const idParam = params.get('id');

        if (idParam === 'new') {
          this.handleNewNoteRoute();
        } else if (idParam) {
          this.handleExistingNoteRoute(idParam);
        } else {
          this.handleNoNoteRoute();
        }
      });
  }

  private handleNewNoteRoute(): void {
    this.isNewNote.set(true);
    const newNote: Note = {
      title: 'New Note',
      content: ''
    };
    this.currentNote.set(newNote);
  }

  private handleExistingNoteRoute(idParam: string): void {
    const id = parseInt(idParam, 10);
    this.isNewNote.set(false);
    this.noteSelectionService.selectNote(id);
    this.loadNote(id);
  }

  private handleNoNoteRoute(): void {
    this.isNewNote.set(false);
    this.currentNote.set(null);
  }

  private loadNote(id: number): void {
    this.noteService.getNote(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (note) => {
          this.currentNote.set(note);
        },
        error: (error) => {
          this.handleOperationError('Error loading note', error);
        }
      });
  }

  private createNoteRequest(): void {
    const note = this.currentNote();
    if (!note) return;

    this.noteService.createNote(note)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (createdNote) => {
          this.handleCreateSuccess(createdNote);
        },
        error: (error) => {
          this.handleOperationError('Error creating note', error);
        }
      });
  }

  private updateNoteRequest(): void {
    const note = this.currentNote();
    if (!note) return;

    this.noteService.updateNote(note)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (updatedNote) => {
          this.handleUpdateSuccess(updatedNote);
        },
        error: (error) => {
          this.handleOperationError('Error saving note', error);
        }
      });
  }

  private handleCreateSuccess(createdNote: Note): void {
    this.currentNote.set(createdNote);
    this.isNewNote.set(false);
    this.isSaving.set(false);
    this.snackBar.open('Note created successfully', 'Close', { duration: 3000 });
    this.noteSelectionService.notifyNotesChanged();
    this.noteSelectionService.selectNote(createdNote.id);
    this.router.navigate(['/notes', createdNote.id]);
  }

  private handleUpdateSuccess(updatedNote: Note): void {
    this.currentNote.set(updatedNote);
    this.isSaving.set(false);
    this.snackBar.open('Note saved successfully', 'Close', { duration: 3000 });
    this.noteSelectionService.notifyNotesChanged();
  }

  private handleDeleteSuccess(): void {
    this.snackBar.open('Note deleted successfully', 'Close', { duration: 3000 });
    this.noteSelectionService.notifyNotesChanged();
    this.currentNote.set(null);
    this.router.navigate(['/notes']);
  }

  private handleOperationError(message: string, error: any): void {
    let errorMessage = message;
    if (error.status === 400 && error.error?.details?.length > 0) {
      errorMessage = error.error.details.map((detail: any) => detail.field + " " + detail.message).join(', ');
    }
    this.isSaving.set(false);
    this.snackBar.open(errorMessage, 'Close', { duration: 3000 });
  }
}
