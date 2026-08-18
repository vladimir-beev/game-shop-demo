import { useState } from "react";

export default function DescriptionSection({ description }) {
    const [open, setOpen] = useState(false);

    return (
        <div className="mx-10">
            <button
                onClick={() => setOpen(!open)}
                className="flex gap-3 cursor-pointer items-center font-bold text-lg mb-2"
            >
                Description
                <span className="text-xl">{open ? "▾" : "▸"}</span>
            </button>

            <div className={`transition-all duration-300 overflow-hidden
                        ${open ? "max-h-[500px] opacity-100" : "max-h-0 opacity-0"}`}
            >
                <div className="max-w-[800px] bg-blue-100 border-2 border-yellow-500 rounded-lg p-6 shadow-lg">
                    <p className="text-gray-800 font-semibold text-justify [text-align-last:left]">
                        {description}
                    </p>
                </div>
            </div>
        </div>
    );
}
