export class ModificationResponse {
  modificationsByDate: ModificationsByDate[];
}

export class ModificationsByDate {
  year: number;
  month: number;
  addedLines: number;
  removedLines: number;
}
