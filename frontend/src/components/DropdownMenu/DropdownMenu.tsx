import { useState } from "react";

export default function DropdownMenu({ children }) {
  const [isOpen, setIsOpen] = useState(false);

  const arrow = "↓";

  return (
    <div>
      <button onClick={() => setIsOpen(!isOpen)}>{arrow}</button>
      {isOpen && <>{children}</>}
    </div>
  );
}
