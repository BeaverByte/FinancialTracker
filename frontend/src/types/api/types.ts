import { PaginationState } from "@tanstack/react-table";

export type PaginatedData<Type> = {
  result: Type[];
  rowCount: number;
};

export type PaginationParams = PaginationState;
export type SortParams = { sortBy: `${string}.${"asc" | "desc"}` };
export type Filters<Type> = Partial<Type & PaginationParams & SortParams>;
