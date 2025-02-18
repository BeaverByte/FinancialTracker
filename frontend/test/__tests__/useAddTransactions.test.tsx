import { renderHook, waitFor } from "@testing-library/react";
import { describe, expect } from "vitest"; // For mocking

import { test } from "../../src/mocks/test-extend";
import { useAddTransaction } from "../../src/hooks/useAddTransaction";
import { createTestQueryWrapper } from "./utility/TestQueryWrapper";

describe("useAddTransaction", () => {
  test("Should succeed to give response", async () => {
    const { result } = renderHook(() => useAddTransaction(), {
      wrapper: createTestQueryWrapper(),
    });

    const mockTransaction = {
      date: "2025-02-11",
      merchant: "Amazon",
      account: "Credit Card - Chase",
      category: "Electronics",
      amount: 120.99,
      note: "Purchased a new pair of headphones",
    };

    result.current.mutate(mockTransaction);

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data).toBeDefined();
  });
});
