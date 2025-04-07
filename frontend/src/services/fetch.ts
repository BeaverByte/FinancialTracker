import { NetworkError, UnauthorizedError } from "./errors";

export const fetchData = async <Type>(
  url: string,
  options: RequestInit = {}
): Promise<Type> => {
  try {
    console.log(
      `Fetching Data at ${url} with options of ${JSON.stringify(options)}`
    );
    const response = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(options.headers || {}),
      },
      credentials: "include",
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new UnauthorizedError("Session expired. Please log in again.");
      }
      throw new Error(`Failed to fetch: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`Error fetching data: ${error}`);
    if (error instanceof TypeError) {
      throw new NetworkError(
        "Could not connect to server. Please check your internet connection."
      );
    }
    throw error;
  }
};
