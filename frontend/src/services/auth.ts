// HTTP Request to login and authenticate an user
export const loginUser = async (username, password) => {
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error("Invalid credentials");
    }

    const data = await response.json();
    // Assuming the response contains a token and user object
    return { user: data.user, token: data.token };
  } catch (error) {
    console.error("Login error:", error);
    throw error;
  }
};
