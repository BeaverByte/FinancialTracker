import { API_ROUTES } from "../utils/API_ROUTES";

// HTTP Request to login and authenticate an user
export const loginUser = async (username: string, password: string) => {
  try {
    const response = await fetch(`${API_ROUTES.AUTH.SIGN_IN}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error("Invalid credentials");
    }

    const data = await response.json();

    console.log("User Login success");

    return { user: data.username };
  } catch (error) {
    console.error("Login error:", error);
    throw error;
  }
};

export const logoutUser = async () => {
  try {
    const response = await fetch(`${API_ROUTES.AUTH.SIGN_OUT}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Could not logout");
    }

    const data = await response.json();

    console.log("User Logout success");

    return { data };
  } catch (error) {
    console.error("Logout error:", error);
    throw error;
  }
};
