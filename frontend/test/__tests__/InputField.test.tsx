import { render, screen, waitFor } from "@testing-library/react";
import { InputField } from "../../src/components/Form/Input";
import "@testing-library/jest-dom";
import { test } from "../../src/mocks/test-extend";
import { expect } from "vitest";

test("Has correct label", async () => {
  // ARRANGE
  render(<InputField name="username" value="" onChange={() => {}} />);

  waitFor(() => expect(screen.findByText("Username:")).toBeInTheDocument);
});
