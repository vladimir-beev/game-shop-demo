import { useState } from "react";

export default function GamesSearchBar({ setTitleFilter }) {
  const [searchTerm, setSearchTerm] = useState("");

  const handleInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearch = () => {
    if (searchTerm == "") {
        setTitleFilter(null);
    }

    setTitleFilter(searchTerm.trim());
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  return (
    <div className="flex w-full">
      <input
        type="text"
        value={searchTerm}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        placeholder="Search for titles"
        className="w-full p-2 border border-gray-200 shadow-md rounded-md
                   bg-white focus:outline-none focus:ring-2 focus:ring-yellow-500"
      />

      <button
        className="ml-3 px-6 py-2 bg-blue-600 shadow-md text-white font-semibold
                   rounded-md hover:bg-blue-700 cursor-pointer"
        onClick={handleSearch}
      >
        Search
      </button>
    </div>
  );
}