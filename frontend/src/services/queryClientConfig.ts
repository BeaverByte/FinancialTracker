import { MutationCache, QueryCache, QueryClient } from "@tanstack/react-query";
import { UnauthorizedError, NetworkError } from "./errors";

// Although Tanstack Query defaults to 3 retries (after 1st failed request), created this variable for config
const queryRetryCount = 2;
// Global callback will always be called and cannot be overwritten by individual QueryClient 'defaultOptions'
export const globalQueryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // No additional retries when "falsy"
      retry: (failureCount, error) => {
        if (failureCount >= queryRetryCount) {
          return false;
        }
        if (error instanceof UnauthorizedError) {
          return false;
        }
        if (error instanceof NetworkError) {
          return true;
        }

        return true;
      },
    },
  },

  // Queries and mutations are handled with their own caches
  queryCache: new QueryCache({
    onError: (error, query) => {
      const failureMessage = `Global Query Client caught error in NON-Mutation Query named "${query.queryKey}": 
      (${error})`;

      console.error(failureMessage, query);

      if (error instanceof UnauthorizedError) {
        console.error(
          failureMessage,
          query,
          `Query error due to Unauthorized user status`
        );
      }
      if (error instanceof NetworkError) {
        console.error(
          failureMessage,
          query,
          `Query error due to server connection failures`
        );
      }
    },
  }),
  mutationCache: new MutationCache({
    onError: (error, query) => {
      const failureMessage = `Global Query Client caught error in MUTATION Query named "${query.queryKey}": 
      (${error})`;

      console.error(failureMessage, query);

      if (error instanceof UnauthorizedError) {
        console.error(
          failureMessage,
          query,
          `Query error due to Unauthorized user status`
        );
      }
      if (error instanceof NetworkError) {
        console.error(
          failureMessage,
          query,
          `Query error due to server connection failures`
        );
      }
    },
  }),
});
