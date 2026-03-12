import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'https://hospitalmanagementsystem-fd2h.onrender.com'
});

export default api;
