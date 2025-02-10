import { test as testBase } from "vitest";
import { worker } from "./browser.js";

export const test = testBase.extend({
  worker: [
    async ({}, use) => {
      // Start the worker before the test.
      await worker.start();

      // Expose the worker object on the test's context.
      await use(worker);

      // Stop the worker after the test is done.
      worker.stop();
    },
    {
      auto: true,
    },
  ],
});
