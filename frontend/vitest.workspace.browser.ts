import { defineWorkspace } from "vitest/config";

export default defineWorkspace([
  // If you want to keep running your existing tests in Node.js, uncomment the next line.
  // 'vitest.config.ts',
  {
    extends: "vitest.config.ts",
    test: {
      name: "browser",
      browser: {
        enabled: true,
        provider: "playwright",
        // https://vitest.dev/guide/browser/playwright
        configs: [],
      },
    },
  },
  // {
  //   test: {
  //     name: "node",
  //     include: ["tests/**/*.{node}.test.{ts,js}"],
  //     environment: "node",
  //   },
  // },
]);
