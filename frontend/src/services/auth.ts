import { User } from "../types/User";
import { API_ROUTES } from "../utils/API_ROUTES";
import { UnauthorizedError } from "./errors";
import { fetchData } from "./fetch";

// HTTP Request to login and authenticate an user
export const loginUser = async (username: string, password: string) => {
  try {
    const response = await fetchData<User>(`${API_ROUTES.AUTH.SIGN_IN}`, {
      method: "POST",
      body: JSON.stringify({ username, password }),
    });

    console.log("User Login success");

    return { user: response.username };
  } catch (error) {
    if (error instanceof UnauthorizedError) {
      throw error;
    }
    throw new Error(`Unexpected error in login: ${error}`);
  }
};

export const logoutUser = async () => {
  try {
    const response = await fetchData(`${API_ROUTES.AUTH.SIGN_OUT}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    console.log(`Logout successful: ${JSON.stringify(response)}`);

    return { response };
  } catch (error) {
    console.error("Logout error:", error);
    throw error;
  }
};
