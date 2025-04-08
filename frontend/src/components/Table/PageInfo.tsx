type PageInfoProps = {
  currentPage: number;
  totalPages: number;
};
export function PageInfo({ currentPage, totalPages }: PageInfoProps) {
  return (
    <span className="flex items-center gap-1">
      <span>Page</span>
      <span>
        {currentPage} of {totalPages}
      </span>
    </span>
  );
}
