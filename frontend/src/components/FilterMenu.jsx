import PlatformFilter from "./PlatformFilter";
import PriceFilter from "./PriceFilter";

function FilterMenu({ setPlatformFilter, setPriceFilter }) {
  const baseButtonStyle =
    "text-white font-bold py-2 px-4 border border-gray-300 rounded hover:cursor-pointer";

  return (
    <div className="bg-gray-200 p-4 shadow-lg rounded-lg max-w-[200px]">
      <h2 className="text-xl font-bold mb-4 text-center">Filters</h2>
      <hr className="border-t border-gray-400 mb-4" />
      <PlatformFilter setPlatformFilter={setPlatformFilter} />
      <hr className="border-t border-gray-400 mb-4" />
      <PriceFilter setPriceFilter={setPriceFilter} />
    </div>
  );
}

export default FilterMenu;
