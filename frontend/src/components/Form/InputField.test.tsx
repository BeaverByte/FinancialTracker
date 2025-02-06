import { describe, expect, test } from "@jest/globals";
import { render } from "@testing-library/react";
import { InputField } from "./Input";

test("Good test name", () => {
  // renderer.create is out of date, need to update to react-testing-library
  render(
    <InputField name="username" value="" onChange={() => {}} errors={errors} />
  );
});
