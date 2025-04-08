import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";

type TransactionsDataTableSelectProps = {
  value: string;
  pageSize: number[];
  onChange: (value: string) => void;
};

export function TransactionsDataTableSelect({
  value,
  pageSize,
  onChange,
}: Readonly<TransactionsDataTableSelectProps>) {
  return (
    <Select onValueChange={onChange} value={value}>
      <SelectTrigger className="w-[180px]">
        <SelectValue placeholder={`Show ${value}`} />
      </SelectTrigger>
      <SelectContent>
        <SelectGroup>
          {pageSize.map((size) => (
            <SelectItem key={size} value={size.toString()}>
              Show {size}
            </SelectItem>
          ))}
        </SelectGroup>
      </SelectContent>
    </Select>
  );
}
