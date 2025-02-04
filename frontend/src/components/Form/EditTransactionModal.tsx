import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

export function EditTransactionModal({ transaction, onClose, onSave }) {
  const [formData, setFormData] = useState(transaction);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div>
      <div>
        <h2>Edit Transaction</h2>

        {Object.keys(transaction)
          .filter((key) => key !== "id")
          .map((key) => (
            <div key={key}>
              <label>{key}:</label>
              <input
                type="text"
                name={key}
                value={formData[key]}
                onChange={handleChange}
              />
            </div>
          ))}

        <div>
          <button onClick={onClose}>Cancel</button>
          <button onClick={() => onSave(formData)}>Save</button>
        </div>
      </div>
    </div>
  );
}
