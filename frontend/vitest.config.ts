import { defineConfig } from "vitest/config";
import AutoImport from "unplugin-auto-import/vite";

// This has higher priority and will override vite.config.ts, anything from vite.config will be ignored
export default defineConfig({
  plugins: [
    AutoImport({
      imports: ["vitest"],
      dts: true, // generate TypeScript declaration
    }),
  ],
  test: {
    name: "dumpyname",
    globals: true,
    environment: "jsdom",
    setupFiles: ["vitest-setup.ts"],
    coverage: {
      provider: "v8",
    },
    browser: {
      enabled: true,
      provider: "playwright",
      instances: [{ browser: "chromium" }],
    },
  },
});
