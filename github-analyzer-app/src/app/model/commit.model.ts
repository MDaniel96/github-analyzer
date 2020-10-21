import {Developer} from './developer.model';

export class Commit {
  id: string;
  author: Developer;
  message: string;
  date: Date;
  modifiedFiles: number;
  addedLines: number;
  removedLines: number;
}
