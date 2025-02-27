export type TableProps = {
  data: Transaction[];
  headers?: TableHeaderConfig[];
  onEditTransaction: (id: number) => void;
};

export type TableHeaderConfig = {
  label: string;
  field: keyof Transaction | "actions";
  visible: boolean;
};

export type Transaction = {
  id: number | string;
  date: string;
  merchant: string;
  account: string;
  category: string;
  amount: number | string;
  note: string;
};
