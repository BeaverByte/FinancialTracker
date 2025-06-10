import { defineConfig } from "vitest/config";
import AutoImport from "unplugin-auto-import/vite";
import path from "path";

// This has higher priority and will override vite.config.ts, anything from vite.config will be ignored
export default defineConfig({
  plugins: [
    AutoImport({
      imports: ["vitest"],
      dts: true, // generate TypeScript declaration
    }),
  ],
  test: {
    name: "frontend tests",
    globals: true,
    environment: "jsdom",
    reporters: ["html"],
    setupFiles: ["vitest-setup.ts"],
    coverage: {
      provider: "v8",
    },
    browser: {
      enabled: true,
      headless: true,
      provider: "playwright",
      instances: [{ browser: "chromium" }],
    },
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
});
