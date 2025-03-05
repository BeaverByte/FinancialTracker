import {
  getRouteApi,
  RegisteredRouter,
  RouteIds,
  useNavigate,
} from "@tanstack/react-router";
import { cleanEmptyParams } from "../components/Table/cleanEmptyParams";

// Hook to update params
export function useFilters<
  Type extends RouteIds<RegisteredRouter["routeTree"]>,
>(routeId: Type) {
  // Using getRouteApi here to avoid importing Route config for access to typed useSearch() hook
  const routeApi = getRouteApi<Type>(routeId);
  const navigate = useNavigate();
  // returns route's search query params
  const filters = routeApi.useSearch();

  console.log("useFilters hook currently " + JSON.stringify(filters));

  // Params are updated unless Params are empty
  const setFilters = (partialFilters: Partial<typeof filters>) =>
    navigate({
      search: (prev) => cleanEmptyParams({ ...prev, ...partialFilters }),
    });

  const resetFilters = () => navigate({ search: {} });

  return { filters, setFilters, resetFilters };
}
