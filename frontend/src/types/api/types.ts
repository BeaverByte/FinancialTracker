import { PaginationState } from "@tanstack/react-table";

export type PaginatedData<Type> = {
  result: Type[];
  rowCount: number;
};

export type PaginationParams = PaginationState;
export type SortParams = { sortBy: `${string}.${"asc" | "desc"}` };
export type Filters<T> = Partial<T & PaginationParams & SortParams>;
