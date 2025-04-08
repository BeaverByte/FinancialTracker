import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  OnChangeFn,
  PaginationOptions,
  PaginationState,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { DebouncedInput } from "../DebouncedInput";
import { Filters } from "../../types/api/types";
import { ArrowDownUp, MoveDown, MoveUp } from "lucide-react";
import { Button } from "../ui/button";
import { TransactionsDataTableSelect } from "./TransactionsDataTableSelect";
import { Input } from "../ui/input";
import { PageInfo } from "./PageInfo";

export const DEFAULT_PAGE_INDEX = 0;
export const DEFAULT_PAGE_SIZE = 10;

type Props<T extends Record<string, string | number>> = {
  data: T[];
  columns: ColumnDef<T>[];
  pagination: PaginationState;
  paginationOptions: Pick<PaginationOptions, "onPaginationChange" | "rowCount">;
  filters: Filters<T>;
  onFilterChange: (dataFilters: Partial<T>) => void;
  sorting: SortingState;
  onSortingChange: OnChangeFn<SortingState>;
};

export default function TransactionsDataTable<
  T extends Record<string, string | number>,
>({
  data,
  columns,
  pagination,
  paginationOptions,
  filters,
  onFilterChange,
  sorting,
  onSortingChange,
}: Readonly<Props<T>>) {
  const table = useReactTable({
    data,
    columns,
    state: { pagination, sorting },
    onSortingChange,
    enableMultiSort: true,
    // Necessary in order for Tanstack sorting behavior to work with null fields
    sortDescFirst: true,
    ...paginationOptions,
    manualFiltering: true,
    enableColumnFilters: false,
    manualSorting: true,
    manualPagination: true,
    enableSortingRemoval: true,
    getCoreRowModel: getCoreRowModel(),
    // debugTable: true,
  });

  return (
    <div>
      <table>
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                const fieldMeta = header.column.columnDef.meta;
                return (
                  <th key={header.id} colSpan={header.colSpan}>
                    {header.isPlaceholder ? null : (
                      <>
                        <Button
                          {...{
                            className: header.column.getCanSort()
                              ? "cursor-pointer select-none"
                              : "",
                            // Handler will toggle column sorting state
                            onClick: header.column.getToggleSortingHandler(),
                            // onClick: header.column.getToggleSortingHandler(),
                            title: header.column.getCanSort()
                              ? header.column.getNextSortingOrder() === "asc"
                                ? "Sort ascending"
                                : header.column.getNextSortingOrder() === "desc"
                                  ? "Sort descending"
                                  : "Clear sort"
                              : undefined,
                          }}
                        >
                          {
                            //  Necessary to flexRender since using cell: () => JSX column definition options
                            flexRender(
                              header.column.columnDef.header,
                              header.getContext()
                            )
                          }
                          {(header.column.getCanSort() &&
                            {
                              asc: <MoveUp size={20} />,
                              desc: <MoveDown size={20} />,
                              false: <ArrowDownUp />,
                            }[header.column.getIsSorted() as string]) ??
                            null}
                        </Button>
                        {header.column.getCanFilter() &&
                        fieldMeta?.filterKey !== undefined ? (
                          <DebouncedInput
                            className="w-36 border shadow rounded"
                            onChange={(value) => {
                              onFilterChange({
                                [fieldMeta.filterKey as keyof T]: value,
                              } as Partial<T>);
                            }}
                            placeholder="Search..."
                            type={
                              fieldMeta.filterVariant === "number"
                                ? "number"
                                : "text"
                            }
                            value={filters[fieldMeta.filterKey] ?? ""}
                          />
                        ) : null}
                      </>
                    )}
                  </th>
                );
              })}
            </tr>
          ))}
        </thead>
        <tbody>
          {table.getRowModel().rows.map((row) => {
            return (
              <tr key={row.id}>
                {row.getVisibleCells().map((cell) => {
                  return (
                    <td key={cell.id}>
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
      <div className="flex items-center gap-2 my-2">
        <Button
          className="cursor-pointer select-none"
          onClick={() => table.setPageIndex(0)}
          disabled={!table.getCanPreviousPage()}
        >
          {"<<"}
        </Button>
        <Button
          className="cursor-pointer select-none"
          onClick={() => table.previousPage()}
          disabled={!table.getCanPreviousPage()}
        >
          {"<"}
        </Button>
        <Button
          className="cursor-pointer select-none"
          onClick={() => table.nextPage()}
          disabled={!table.getCanNextPage()}
        >
          {">"}
        </Button>
        <Button
          className="cursor-pointer select-none"
          onClick={() => table.setPageIndex(table.getPageCount() - 1)}
          disabled={!table.getCanNextPage()}
        >
          {">>"}
        </Button>
        <PageInfo
          currentPage={table.getState().pagination.pageIndex + 1}
          totalPages={table.getPageCount()}
        />
        <span className="flex items-center gap-1">
          | Go to page:
          <Input
            type="number"
            value={table.getState().pagination.pageIndex + 1}
            onChange={(e) => {
              const page = e.target.value ? Number(e.target.value) - 1 : 0;
              table.setPageIndex(page);
            }}
            className="border p-1 rounded w-16"
          />
        </span>
        <TransactionsDataTableSelect
          value={table.getState().pagination.pageSize.toString()}
          pageSize={[10, 20, 30, 40, 50]}
          onChange={(value: string) => {
            table.setPageSize(Number(value));
          }}
        />
      </div>
    </div>
  );
}
