const placeholderBg = "bg-[#bfc4c8]";

export default function GameDetailsSkeleton() {
  return (
    <div className="flex flex-1 flex-col mx-auto bg-gray-200/70">
      <div className="flex flex-wrap gap-8 px-10 py-10 animate-pulse">
        {/* Image placeholder */}
        <div className={`flex items-center justify-center w-[300px] h-[350px] rounded-lg ${placeholderBg}`} />
        {/* Right side content */}
        <div className="flex flex-col gap-2 w-[400px]">
          <div className={`h-8 w-64 rounded mb-2 ${placeholderBg}`} />
          <hr className="border-t border-gray-400 mb-4" />
          <div className={`h-5 w-40 rounded ${placeholderBg}`} />
          <div className={`h-5 w-32 rounded ${placeholderBg}`} />
          {/* Description */}
          <div className={`h-5 w-full rounded ${placeholderBg}`} />
          <div className={`h-5 w-[90%] rounded ${placeholderBg}`} />
          <div className={`h-5 w-[80%] rounded ${placeholderBg}`} />
          <hr className="border-t border-gray-400 mb-4" />
          <div className={`h-5 w-48 rounded ${placeholderBg}`} />
          <div className={`mt-8 h-10 w-[150px] rounded-md ${placeholderBg}`} />
        </div>
      </div>
      <div className="flex px-10 animate-pulse">
        {/* Description */}
        <div className={`h-5 w-[200px] rounded ${placeholderBg}`} />
      </div>
    </div>
  );
}
