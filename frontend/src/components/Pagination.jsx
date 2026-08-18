export default function Pagination({ page, onPageChange }) {
  if (!page) {
    return null;
  }

  const { number, totalPages, first, last } = page;
  const buttonStyle = "px-3 py-1 bg-gray-200 rounded-md border border-gray-300 shadow cursor-pointer disabled:opacity-50 hover:bg-gray-100 transition";

  const getPageNumbers = () => {
    const pages = [];
    const maxButtons = 5;

    let start = Math.max(0, number - 2);
    let end = Math.min(totalPages - 1, number + 2);

    // Ensure exactly 5 buttons when possible
    if (end - start < maxButtons - 1) {
      if (start === 0) {
        end = Math.min(totalPages - 1, start + (maxButtons - 1));
      } 
      else if (end === totalPages - 1) {
        start = Math.max(0, end - (maxButtons - 1));
      }
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    return pages;
  };

  const pages = getPageNumbers();

  return (
    <div className="flex justify-center items-center gap-2 mt-9">

      {/* First */}
      <button
        disabled={first}
        onClick={() => onPageChange(0)}
        className={buttonStyle}
      >
        &laquo;
      </button>

      {/* Previous */}
      <button
        disabled={first}
        onClick={() => onPageChange(number - 1)}
        className={buttonStyle}
      >
        &lsaquo;
      </button>

      {/* Numbered buttons */}
      {pages.map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          className={`px-3 py-1 rounded-md shadow transition cursor-pointer ${
            pageNum === number
              ? "bg-[#f6a731] border border-yellow-500 text-white font-bold"
              : "bg-gray-200 border border-gray-300 hover:bg-gray-100"
          }`}
        >
          {pageNum + 1}
        </button>
      ))}

      {/* Next */}
      <button
        disabled={last}
        onClick={() => onPageChange(number + 1)}
        className={buttonStyle}
      >
        &rsaquo;
      </button>

      {/* Last */}
      <button
        disabled={last}
        onClick={() => onPageChange(totalPages - 1)}
        className={buttonStyle}
      >
        &raquo;
      </button>
    </div>
  );
}
