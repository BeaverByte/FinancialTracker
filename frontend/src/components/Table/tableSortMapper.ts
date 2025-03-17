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
  // const sort = sorting[0];
  const sortParams = sorting
    .map((sort) => `${sort.id}.${sort.desc ? "desc" : "asc"}`)
    .join(",");

  console.log(
    "State converted to SortBy for URL: " + JSON.stringify(sortParams)
  );

  // return `${sort.id}.${sort.desc ? "desc" : "asc"}` as const;
  return sortParams;
};

export const convertSortByInURLToState = (
  sortBy: SortParams["sortBy"] | undefined
) => {
  console.log("Sortby to State is " + sortBy);
  if (!sortBy) return [];

  // const [id, desc] = sortBy.split(".");
  // return [{ id, desc: desc === "desc" }];

  return sortBy.split(",").map((sort) => {
    const [id, desc] = sort.split(".");
    return { id, desc: desc === "desc" };
  });
};
