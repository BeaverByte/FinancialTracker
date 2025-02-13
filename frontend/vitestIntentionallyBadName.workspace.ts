// import { defineWorkspace } from "vitest/config";

// export default defineWorkspace([
//   // If you want to keep running your existing tests in Node.js, uncomment the next line.
//   // "vitest.config.ts",
//   {
//     extends: "vitest.config.ts",
//     test: {
//       name: "browser",
//       include: ["tests/**/*.{node}.test.{ts,js}"],
//       // Browser is not considered an environment in Vitest
//       browser: {
//         enabled: true,
//         // headless: true,
//         // Playwright allows for Chrome DevTools Protocol for testing
//         // https://vitest.dev/guide/browser/playwright
//         provider: "playwright",
//         // instances: [{ browser: "chromium" }],
//       },
//     },
//   },
//   {
//     test: {
//       name: "node",
//       include: ["tests/**/*.{node}.test.{ts,js}"],
//       environment: "node",
//     },
//   },
// ]);
