type TableColumnProps = {
  children: React.ReactNode;
};

function TableColumn({ children }: Readonly<TableColumnProps>) {
  return <td>{children}</td>;
}

export default TableColumn;
