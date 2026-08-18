import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_GATEWAY_URL || "http://localhost:8080",
  withCredentials: true,
});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(promise => {
    if (error) {
        promise.reject(error);
    }
    else {
        promise.resolve(token);
    }
  });

  failedQueue = [];
};

export const setupInterceptors = (refresh, getAccessToken) => {

  api.interceptors.request.use(config => {
    const token = getAccessToken();

    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }

    return config;
  });

  api.interceptors.response.use(
    response => response,
    async error => {
      const originalRequest = error.config;

      if (error.response?.status === 401 && !originalRequest._retry) {
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          }).then((token) => {
            originalRequest.headers["Authorization"] = "Bearer " + token;
            return api(originalRequest);
          });
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
          const newToken = await refresh();

          api.defaults.headers.common["Authorization"] = "Bearer " + newToken;

          processQueue(null, newToken);

          originalRequest.headers["Authorization"] = "Bearer " + newToken;
          
          return api(originalRequest);
        } 
        catch (refreshError) {
          processQueue(refreshError, null);
          throw refreshError;
        } 
        finally {
          isRefreshing = false;
        }
      }

      throw error;
    }
  );
};

export default api;
