import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import api from "../api";

export default function OrdersPage() {
  const { accessToken } = useContext(AuthContext);

  const [loading, setLoading] = useState(true);
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [isOpenHistory, setIsOpenHistory] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const statusColor = {
    PENDING: "text-blue-600",
    COMPLETED: "text-green-600",
    CANCELLED: "text-red-600",
  };

  useEffect(() => {
    fetchPendingOrders();
  }, [accessToken]);

  async function fetchPendingOrders() {
    if (!accessToken) {
      return;
    }

    try {
      const response = await api.get("/orders/pending");
      const data = response.data;
      setOrders(data);
    }
    catch (error) {
      console.error("Error fetching pending orders:", error);
    }

    setLoading(false);
  }

  async function fetchOrderHistory() {
    if (!accessToken) {
      return;
    }

    try {
      const response = await api.get("/orders/history");
      const data = response.data;
      setOrders(data);
    } 
    catch (error) {
      console.error("Error fetching order history:", error);
    }

    setLoading(false);
  }

  async function handleHistoryPendingClick() {
    if (!isOpenHistory) {
      setIsOpenHistory(true);
      await fetchOrderHistory();
    } 
    else {
      setIsOpenHistory(false);
      await fetchPendingOrders();
    }
  }

  async function loadOrderDetails(orderId) {
    try {
      const response = await api.get(`/orders/details/${orderId}`);
      const data = response.data;
      setSelectedOrder(data);
    }
    catch (error) {
      console.error("Error fetching order details:", error);
    }
  }

  async function openModal(order) {
    setLoading(true);
    await loadOrderDetails(order.id);
    setLoading(false);
    setIsModalOpen(true);
  }

  function closeModal() {
    setIsModalOpen(false);
    setSelectedOrder(null);
  }

  return (
    <div className="flex flex-1 flex-col px-10 bg-gray-300 p-6">
      <div className="flex flex-wrap justify-between items-center mx-8 mb-4">
          <h1 className="text-3xl font-bold">My Orders</h1>
          <button className="text-xl text-gray-800 bg-gray-100 px-2 py-1 rounded-md border-2 border-gray-500 
              font-semibold cursor-pointer hover:border-blue-800 hover:text-blue-800 transition-colors duration-300"
              onClick={() => handleHistoryPendingClick()}
          >
            {isOpenHistory ? 'Show Pending' : 'Show History'}
          </button>
      </div>
      <hr className="border-t border-gray-400 mb-5" />
      {orders.length === 0 ? (
        <p className="text-center text-lg font-semibold text-gray-700">No orders found.</p>
      ) : (
        <div className="space-y-5 px-8">
          {orders.map((order) => (
            <div
              key={order.id}
              className="bg-gray-100 px-6 py-4 gap-6 rounded-md shadow-lg"
            >
              <div className="flex flex-wrap justify-between mb-2">
                <p className="text-lg font-semibold">
                  <strong>Order #:</strong> {order.id}
                </p>
                <p className="text-lg font-semibold">
                  <strong>Order Date:</strong>{" "}
                  {new Date(order.createdAt).toLocaleString()}
                </p>
                <p
                  className="text-lg text-blue-900 font-semibold cursor-pointer underline hover:text-blue-700"
                  onClick={() => openModal(order)}
                >
                  Details
                </p>
              </div>
              <div className="flex flex-wrap gap-8">
                  <p className="text-lg font-semibold">
                    <strong>Status:</strong> <span className={statusColor[order.status] || 'text-gray-600'}>{order.status}</span>
                  </p>
                  {order.status === 'CANCELLED' && (
                    <p className="text-lg font-semibold">
                      <strong>Reason:</strong> <span className="text-gray-600">{order.cancellationReason}</span>
                    </p>
                  )}
              </div>
            </div>
          ))}
        </div>
      )}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50">
          <div className="bg-gray-200 p-6 rounded-lg shadow-xl w-[1000px] max-h-[500px]">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-bold">Order Details</h2>
                <button
                  onClick={() => closeModal()}
                  className="text-2xl font-bold text-red-700 cursor-pointer hover:text-red-600"
                >
                  &times;
                </button>
            </div>
            <div className="flex flex-wrap justify-between mx-4">
                <p className="text-lg font-semibold">
                  <strong>Order #:</strong> {selectedOrder.orderId}
                </p>
                <p className="text-lg font-semibold">
                  <strong>Total:</strong> € {Number(selectedOrder.totalPrice).toFixed(2)}
                </p>
            </div>
            <hr className="border-t border-gray-400 mt-2 mb-5" />
            <div className="mx-4 overflow-y-auto max-h-[300px] border-3 border-gray-400 shadow-md">
                {selectedOrder.items.map((item) => (
                  <div key={item.id} className="flex flex-wrap gap-6 bg-white border border-gray-300 p-4 shadow-sm">
                    <p><strong>Title:</strong> {item.title}</p>
                    {item.platform && <p><strong>Platform:</strong> {item.platform}</p>}
                    <p><strong>Quantity:</strong> {item.quantity}</p>
                    <p><strong>Sub-Total:</strong> € {Number(item.subtotal).toFixed(2)}</p>
                  </div>
                ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}