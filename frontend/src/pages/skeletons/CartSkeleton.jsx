const placeholderBg = "bg-[#bfc4c8]";

export default function CartSkeleton() {
  return (
    <div className="flex flex-1 flex-col mx-auto bg-gray-300">
      <div className="flex flex-col gap-2 px-10 py-10 animate-pulse">
        <div className="flex flex-wrap justify-between gap-2 mb-4 px-4">
          <div className={`h-8 w-48 rounded ${placeholderBg}`} />
          <div className="flex flex-wrap gap-6 items-center">
            <div className={`h-10 w-[120px] rounded-lg ${placeholderBg}`} />
            <div className={`h-8 w-32 rounded ${placeholderBg}`} />
          </div>
        </div>
        <hr className="border-t border-gray-400 mb-4" />
        <div className="flex flex-col gap-4 px-4">
          {[1, 2, 3].map((i) => (
            <div
              key={i}
              className="flex flex-wrap justify-between p-4 bg-zinc-300 rounded-lg shadow-lg"
            >
              <div className="flex flex-wrap gap-6">
                <div className={`h-5 w-40 rounded ${placeholderBg}`} />
                <div className={`h-5 w-32 rounded ${placeholderBg}`} />
                <div className={`h-5 w-24 rounded ${placeholderBg}`} />
                <div className={`h-5 w-20 rounded ${placeholderBg}`} />
                <div className={`h-5 w-28 rounded ${placeholderBg}`} />
              </div>
              <div className={`h-6 w-6 rounded ${placeholderBg}`} />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
