import { useState, ChangeEvent } from "react";

interface StateProps {
  /** type of the state */
  type: "error" | "warning";
  /** icon name as used in the `Icon` component */
  icon?: string;
  /** message that explains the state to the user */
  message?: string;
}

type InputProps = {
  /** unique id for input */
  id: string;
  /** value to set on init of the component */
  presetValue?: string;
  /** label text shown above input */
  label?: string;
  /** placeholder text shown when input is empty */
  placeholder?: string;
  /** disable input to prevent modifying value */
  disabled?: boolean;
  /** make input required */
  required?: boolean;
  /** state for validation */
  state?: StateProps;
  /** listen to changeEvent */
  onChange?: (event: ChangeEvent) => void;
};

export const Input = (props: InputProps) => {
  const {
    id,
    presetValue,
    label,
    placeholder,
    disabled,
    required,
    state,
    onChange,
  } = props;

  // store value, fill with 'presetValue' on load if present
  const [inputValue, setInputValue] = useState(presetValue || "");

  return (
    <div className="mb-4">
      <div>
        {label && (
          <div className="block mb-2 text-sm font-bold text-gray-700">
            {label} {required ? "*" : null}
          </div>
        )}
        <input
          className="w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded appearance-none focus:outline-none focus:shadow-outline"
          id={id}
          type="text" // will be a prop in separate issue to create 'email', 'password' etc.
          placeholder={placeholder}
          disabled={disabled}
          value={inputValue}
          onChange={(e) => {
            // call setInputValue to update component state
            setInputValue(e.target.value);
            // call onChange by optional chaining
            onChange?.(e);
          }}
        />

        {/* State message */}
        {state?.message && (
          <div className="text-xs italic text-red-500 mt-2">
            {state.message}
          </div>
        )}
      </div>
    </div>
  );
};
