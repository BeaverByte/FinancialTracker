import { TableHeaderConfig } from "../../types/Transaction";

function TableHeader({ headers }: Readonly<{ headers: TableHeaderConfig[] }>) {
  return (
    <thead>
      <tr>
        {headers
          .filter((header) => header.visible)
          .map((header) => (
            <th key={header.field}>{header.label}</th>
          ))}
      </tr>
    </thead>
  );
}

export default TableHeader;
