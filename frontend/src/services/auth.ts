// HTTP Request to login and authenticate an user
export const loginUser = async (username: string, password: string) => {
  try {
    const response = await fetch("http://localhost:8080/api/auth/signin", {
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
