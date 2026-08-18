import FilterSection from "./FilterSection";

export default function PlatformFilter({ setPlatformFilter }) {
  const baseButtonStyle = "text-white font-bold py-2 px-4 border border-gray-300 rounded-md shadow hover:cursor-pointer";

  return (
    <FilterSection title="Platforms">
      <div className="flex flex-col gap-2">

        <button
          className={`${baseButtonStyle} bg-blue-500 hover:bg-blue-600`}
          onClick={() => setPlatformFilter("all")}
        >
          All Games
        </button>

        <button
          className={`${baseButtonStyle} bg-gray-500 hover:bg-gray-600`}
          onClick={() => setPlatformFilter("PC")}
        >
          PC
        </button>

        <button
          className={`${baseButtonStyle} bg-blue-800 hover:bg-blue-900`}
          onClick={() => setPlatformFilter("PLAYSTATION")}
        >
          PlayStation
        </button>

        <button
          className={`${baseButtonStyle} bg-green-700 hover:bg-green-800`}
          onClick={() => setPlatformFilter("XBOX")}
        >
          Xbox
        </button>

        <button
          className={`${baseButtonStyle} bg-red-600 hover:bg-red-700`}
          onClick={() => setPlatformFilter("NINTENDO")}
        >
          Nintendo
        </button>
      </div>
    </FilterSection>
  );
}
