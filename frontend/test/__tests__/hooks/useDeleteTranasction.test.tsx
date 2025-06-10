import { renderHook, waitFor } from "@testing-library/react";
import { describe, expect } from "vitest"; // For mocking

import { test } from "../../../src/mocks/test-extend";

import { createTestQueryWrapper } from "../utility/TestQueryWrapper";
import { useDeleteTransaction } from "@/hooks/useDeleteTransaction";
import { fakeTransaction } from "@/mocks/handlers";

const id = fakeTransaction.id;

describe("useDeleteTransaction", () => {
  test("Should succeed with response", async () => {
    const { result } = renderHook(() => useDeleteTransaction(), {
      wrapper: createTestQueryWrapper(),
    });

    result.current.mutate(id);

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
  });
});
