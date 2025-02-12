import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { InputField } from "../../src/components/Form/Input";
import "@testing-library/jest-dom";
import { test } from "../../src/mocks/test-extend";
import { expect } from "vitest";

test("Has correct label", async () => {
  // ARRANGE
  render(<InputField name="username" value="" onChange={() => {}} />);

  const label = screen.getByLabelText("Username:");
  // ACT
  await userEvent.click(screen.getByLabelText("Username:"));
  await screen.findByRole("heading");

  // ASSERT
  expect(label).toHaveTextContent("hello there");
  expect(screen.getByRole("button")).toBeDisabled();
});
