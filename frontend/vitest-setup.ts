import "@testing-library/jest-dom/vitest";
// import { server } from "./src/mocks/nodes";
import { beforeAll, afterEach, afterAll } from "vitest";
import { cleanup } from "@testing-library/react";

beforeAll(() => {
  // server.listen();
});

afterEach(() => {
  // server.resetHandlers();
  cleanup();
});

afterAll(() => {
  // server.close();
});
