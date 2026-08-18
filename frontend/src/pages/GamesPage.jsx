import { useEffect, useState } from "react";
import api from "../api";
import Navbar from "../components/Navbar";
import GamesList from "../components/GamesList";
import FilterMenu from "../components/FilterMenu";
import GamesSearchBar from "../components/GamesSearchBar";
import Pagination from "../components/Pagination";
import Alert from "../components/Alert";
import Footer from "../components/Footer";

export default function GamesPage() {
  const [gamesPage, setGamesPage] = useState(null);
  const [loading, setLoading] = useState(true);
  const [titleFilter, setTitleFilter] = useState(null);
  const [platformFilter, setPlatformFilter] = useState("all");
  const [priceFilter, setPriceFilter] = useState({ min: null, max: null });
  const pageSize = 20;

  const fetchGames = async (page) => {
    setLoading(true);

    const params = new URLSearchParams({
      page,
      size: pageSize,
      ...(titleFilter !== null && { title: titleFilter }),
      ...(platformFilter !== "all" && { platform: platformFilter }),
      ...(priceFilter?.min !== null && { minPrice: priceFilter.min }),
      ...(priceFilter?.max !== null && { maxPrice: priceFilter.max }),
    });

    try {
      setLoading(true);
      const response = await api.get(`/products/games?${params.toString()}`);
      setGamesPage(response.status === 204 ? null : response.data);
    } 
    catch (error) {
      setGamesPage(null);

      console.error("Failed to load games:", error.response?.status);
    } 
    finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGames(0);
  }, [titleFilter, platformFilter, priceFilter]);

  return (
    <div className="flex flex-1 flex-col mx-auto bg-gray-200/70">
      <div className=" flex flex-1 flex-col p-6">
          <div className="flex flex-1 gap-6">
              <div className="sticky top-6 h-fit w-[180px]">
                  <FilterMenu setPlatformFilter={setPlatformFilter} setPriceFilter={setPriceFilter} />
              </div>
              <div className="flex flex-1 flex-col min-w-0 gap-3">
                <GamesSearchBar setTitleFilter={setTitleFilter} />
                <div className="flex justify-center">
                  {gamesPage?.content?.length === 0 && !loading && (
                    <Alert message="No games found for the selected filters." />
                  )}
                </div>
                <GamesList games={gamesPage?.content || []} loading={loading} />
              </div>
          </div>
          <Pagination page={gamesPage} onPageChange={fetchGames} />
      </div>
    </div>
  );
}
