import { useState, useEffect } from "react";
import FilterSection from "./FilterSection";

export default function PriceFilter({ setPriceFilter }) {
  const [min, setMin] = useState(null);
  const [max, setMax] = useState(null);

  const baseButtonStyle = "text-white font-bold py-2 px-4 border border-gray-300 rounded-md shadow hover:cursor-pointer";

  const applyRange = () => {
    setPriceFilter({
      min: min !== null && min !== "" ? Number(min) : null,
      max: max !== null && max !== "" ? Number(max) : null,
    });
  };

  const clearRange = () => {
    setMin(null);
    setMax(null);
    setPriceFilter({ min: null, max: null });
  };

  const applyPreset = (minValue, maxValue) => {
    setMin(minValue);
    setMax(maxValue);
    setPriceFilter({
      min: minValue,
      max: maxValue,
    });
  };

  return (
    <FilterSection title="Price Range">
      <div className="flex flex-col gap-3">

        {/* Presets */}
        <div className="flex flex-col gap-2 mt-2">
          <button
            className={`${baseButtonStyle} bg-green-600 hover:bg-green-700`}
            onClick={() => applyPreset(0, 20)}
          >
            Under €20
          </button>

          <button
            className={`${baseButtonStyle} bg-yellow-600 hover:bg-yellow-700`}
            onClick={() => applyPreset(20, 50)}
          >
            €20 - €50
          </button>

          <button
            className={`${baseButtonStyle} bg-orange-600 hover:bg-orange-700`}
            onClick={() => applyPreset(50, 100)}
          >
            €50 - €100
          </button>

          <button
            className={`${baseButtonStyle} bg-red-600 hover:bg-red-700`}
            onClick={() => applyPreset(100, null)}
          >
            €100+
          </button>
        </div>

        {/* Min / Max */}
        <div className="flex flex-col gap-2">
          <input
            type="number"
            placeholder="Min"
            value={min ?? ""}
            onChange={(e) =>
              setMin(e.target.value === "" ? null : e.target.value)
            }
            className="p-2 border border-gray-400 rounded-md"
          />

          <input
            type="number"
            placeholder="Max"
            value={max ?? ""}
            onChange={(e) =>
              setMax(e.target.value === "" ? null : e.target.value)
            }
            className="p-2 border border-gray-400 rounded-md"
          />

          <button
            onClick={applyRange}
            className={`${baseButtonStyle} bg-blue-500 hover:bg-blue-600`}
          >
            Apply
          </button>

          <button
            onClick={clearRange}
            className={`${baseButtonStyle} bg-orange-400 hover:bg-orange-500`}
          >
            Clear
          </button>
        </div>
      </div>
    </FilterSection>
  );
}
