import {
  getRouteApi,
  RegisteredRouter,
  RouteIds,
  useNavigate,
} from "@tanstack/react-router";
import {
  DEFAULT_PAGE_INDEX,
  DEFAULT_PAGE_SIZE,
} from "../components/Table/TransactionsDataTable";

// Hook to update params
export function useFilters<
  Type extends RouteIds<RegisteredRouter["routeTree"]>,
>(routeId: Type) {
  // Using getRouteApi for type safety of provided route
  const routeApi = getRouteApi<Type>(routeId);

  const navigate = useNavigate();

  // returns route's search query params
  const filters = routeApi.useSearch();

  console.log(
    `useFilters hook currently ${JSON.stringify(filters)} in Route: "${routeId}"`
  );
  // Params are updated unless Params are empty
  const setFilters = (partialFilters: Partial<typeof filters>) =>
    navigate({
      search: (prev) => cleanEmptyParams({ ...prev, ...partialFilters }),
    });

  const resetFilters = () => navigate({ search: {} });

  return { filters, setFilters, resetFilters };
}

const cleanEmptyParams = <T extends Record<string, unknown>>(search: T) => {
  const newSearch = { ...search };

  console.log("Evaluating params " + JSON.stringify(newSearch));

  Object.keys(newSearch).forEach((key) => {
    const value = newSearch[key];
    if (
      value === undefined ||
      value === "" ||
      (typeof value === "number" && isNaN(value))
    ) {
      delete newSearch[key];
    }
  });

  if (search.pageIndex === DEFAULT_PAGE_INDEX) delete newSearch.pageIndex;
  if (search.pageSize === DEFAULT_PAGE_SIZE) delete newSearch.pageSize;

  console.log("Cleaned params are now " + JSON.stringify(newSearch));

  return newSearch;
};
