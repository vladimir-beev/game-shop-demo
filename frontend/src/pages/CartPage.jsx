import { useState, useContext, useEffect } from "react";
import { useOutletContext, useNavigate } from "react-router-dom";
import { AuthContext } from "../auth/AuthContext";
import api from "../api";
import CartSkeleton from "./skeletons/CartSkeleton";

export default function CartPage() {
    const navigate = useNavigate();
    const { accessToken } = useContext(AuthContext);
    const setAlert = useOutletContext();

    const [cart, setCart] = useState(null);
    const [cartItems, setCartItems] = useState([]);

    const [loading, setLoading] = useState(true);

    useEffect(() => {
      loadCart();
    }, []);

    async function loadCart() {
      setLoading(true);

      if (!accessToken) return;

      try {
        const cartResponse = await api.get("/cart/items");
        const cartData = cartResponse.data;

        setCart(cartData);
        setCartItems(cartData.items);
        setLoading(false);
      } 
      catch (error) {
        console.error("Error fetching cart:", error);
        setLoading(false);

        setAlert({
          message: "Failed to fetch cart. Please try again.",
          type: "error",
        });
      }
    }

    async function removeItemFromCart(itemId) {
        try {
            await api.delete(`/cart/items/${itemId}`);
        }
        catch (error) {
            console.error("Error removing item from cart:", error);

            setAlert({
            message: "Failed to remove item from cart. Please try again.",
            type: "error",
          });
        }
    }

    async function handleRemoveItem(itemId) {
        await removeItemFromCart(itemId);
        await loadCart();
    }

    async function handleCheckout() {
      async function checkoutCart() {
        try {
          await api.post("/cart/checkout");

          return true;
        } 
        catch (error) {
          console.error("Error during checkout:", error);

          setAlert({
            message: "Checkout failed. Please try again.",
            type: "error",
          });

          return false;
        }
      }

      const success = await checkoutCart();

      if (success) {
        await loadCart();

        setAlert({
          message: "Checkout successful!",
          type: "success",
        });
      }
    }

    if (loading) {
        return(
            <CartSkeleton />
        );
    }

    return (
        <div className="flex flex-1 flex-col mx-auto bg-gray-300">
            {loading ? (
                <p className="ml-6 text-lg font-semibold text-gray-600">Loading cart...</p>
            ) : (
                <div className="flex flex-col gap-2 px-10 py-10">
                    <div className="flex flex-wrap justify-between gap-2 mb-4 px-4">
                        <h1 className="text-3xl font-bold">Cart: {cartItems.length} {cartItems.length === 1 ? 'item' : 'items'}</h1>
                        {cartItems.length > 0 && 
                        <div className="flex flex-wrap gap-6 items-center">
                            <button
                                onClick={handleCheckout}
                                className="px-4 py-1 font-semibold border-3 border-green-600 bg-green-500 text-white rounded-lg shadow-lg 
                                    cursor-pointer hover:bg-green-600 transition-colors duration-300"
                            >
                                Checkout
                            </button>
                            <h1 className="text-3xl font-bold">Total: € {cart?.totalPrice}</h1>
                        </div>}
                    </div>
                    <hr className="border-t border-gray-400 mb-4" />
                    <div className="flex flex-col gap-4 px-4">
                        {cartItems.length === 0 ? (
                            <p className="font-semibold text-xl text-gray-700">
                                Your cart is empty.
                            </p>
                        ) : (
                            cartItems.map((item) => (
                                <div key={item.id} className="flex flex-wrap justify-between p-4 bg-gray-100 rounded-lg shadow-lg">
                                    <div className="flex flex-wrap gap-6">
                                        <p><strong>Title:</strong> {item.title}</p>
                                        {item.platform && <p><strong>Platform:</strong> {item.platform}</p>}
                                        <p><strong>Quantity:</strong> {item.quantity}</p>
                                        <p><strong>Price:</strong> € {item.price}</p>
                                        {item.quantity > 1 && <p><strong>Sub-Total:</strong> € {item.subtotal}</p>}
                                    </div>
                                    <div>
                                        <button onClick={() => handleRemoveItem(item.id)}
                                            className="text-xl font-bold text-red-600 cursor-pointer hover:text-red-500 hover:scale-125"
                                        >
                                            &times;
                                        </button>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}