import { useState } from "react";

export default function FilterSection({ title, children }) {
  const [open, setOpen] = useState(false);

  return (
    <div className="w-full mb-4">
      <button
        onClick={() => setOpen(!open)}
        className="w-full flex gap-3 cursor-pointer items-center font-bold text-lg mb-2"
      >
        {title}
        <span className="text-xl">{open ? "▾" : "▸"}</span>
      </button>

      <div
        className={`transition-all duration-300 overflow-hidden ${
          open ? "max-h-[500px] opacity-100" : "max-h-0 opacity-0"
        }`}
      >
        {children}
      </div>
    </div>
  );
}
