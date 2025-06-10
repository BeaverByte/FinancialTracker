import { PaginationState } from "@tanstack/react-table";

export type PaginatedData<Type> = {
  content: Type[];
  totalElements: number;
};

export type PaginationParams = PaginationState;
type SortDirection = "asc" | "desc";
export type SortByString = `${string}.${SortDirection}`;
export type SortParams = { sortBy: SortByString };
export type Filters<T> = Partial<T & PaginationParams & SortParams>;
