import { SortingState } from "@tanstack/react-table";
import { SortParams } from "../../types/api/types";

export const convertStateToSortByInURL = (
  sorting: SortingState | undefined
) => {
  console.log(
    "State to be converted to SortBy for URL is " + JSON.stringify(sorting)
  );
  if (!sorting || sorting.length == 0) {
    console.log("Sorting is empty returning undefined");
    return undefined;
  }

  // Get first sort
  const sort = sorting[0];

  console.log("State converted to SortBy for URL " + JSON.stringify(sort));

  return `${sort.id}.${sort.desc ? "desc" : "asc"}` as const;
};

export const convertSortByInURLToState = (
  sortBy: SortParams["sortBy"] | undefined
) => {
  console.log("Sortby to State is " + sortBy);
  if (!sortBy) return [];

  const [id, desc] = sortBy.split(".");
  return [{ id, desc: desc === "desc" }];
};
