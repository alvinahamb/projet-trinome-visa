import { useState, useCallback } from 'react';
import axios from 'axios';
import { API_CONFIG, HTTP_STATUS, ERROR_MESSAGES } from '@/api';

/**
 * Configuration de l'instance axios avec l'URL de base depuis .env
 */
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: API_CONFIG.TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

/**
 * Intercepteur de requête - Ajoute le token d'authentification si présent
 */
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Intercepteur de réponse - Gestion centralisée des erreurs
 */
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Erreurs avec réponse du serveur
      switch (error.response.status) {
        case HTTP_STATUS.UNAUTHORIZED:
          // Redirection vers login si non autorisé
          localStorage.removeItem('authToken');
          window.location.href = '/login';
          break;
        case HTTP_STATUS.FORBIDDEN:
          console.error('Accès interdit:', error.response.data);
          break;
        case HTTP_STATUS.NOT_FOUND:
          console.error('Ressource non trouvée:', error.response.data);
          break;
        case HTTP_STATUS.INTERNAL_SERVER_ERROR:
          console.error('Erreur serveur:', error.response.data);
          break;
        default:
          console.error('Erreur API:', error.response.data);
      }
    } else if (error.request) {
      // Requête envoyée mais pas de réponse
      console.error('Pas de réponse du serveur:', error.request);
    } else {
      // Erreur lors de la configuration de la requête
      console.error('Erreur de configuration:', error.message);
    }
    return Promise.reject(error);
  }
);

/**
 * Hook personnalisé pour les appels API avec gestion d'état
 * @returns {object} - { data, loading, error, request }
 */
const useApi = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Fonction générique pour effectuer une requête API
   * @param {string} method - Méthode HTTP (GET, POST, PUT, DELETE, PATCH)
   * @param {string} url - URL de l'endpoint
   * @param {object} payload - Données à envoyer (pour POST, PUT, PATCH)
   * @param {object} config - Configuration supplémentaire axios
   * @returns {Promise} - Résultat de la requête
   */
  const request = useCallback(async (method, url, payload = null, config = {}) => {
    setLoading(true);
    setError(null);
    
    try {
      let response;
      
      switch (method.toUpperCase()) {
        case 'GET':
          response = await apiClient.get(url, config);
          break;
        case 'POST':
          response = await apiClient.post(url, payload, config);
          break;
        case 'PUT':
          response = await apiClient.put(url, payload, config);
          break;
        case 'PATCH':
          response = await apiClient.patch(url, payload, config);
          break;
        case 'DELETE':
          response = await apiClient.delete(url, config);
          break;
        default:
          throw new Error(`Méthode HTTP non supportée: ${method}`);
      }
      
      setData(response.data);
      return response.data;
      
    } catch (err) {
      const errorMessage = err.response?.data?.message 
        || err.message 
        || ERROR_MESSAGES.UNKNOWN_ERROR;
      
      setError({
        message: errorMessage,
        status: err.response?.status,
        data: err.response?.data
      });
      
      throw err;
      
    } finally {
      setLoading(false);
    }
  }, []);

  /**
   * Méthodes raccourcies pour chaque type de requête
   */
  const get = useCallback((url, config) => request('GET', url, null, config), [request]);
  const post = useCallback((url, payload, config) => request('POST', url, payload, config), [request]);
  const put = useCallback((url, payload, config) => request('PUT', url, payload, config), [request]);
  const patch = useCallback((url, payload, config) => request('PATCH', url, payload, config), [request]);
  const del = useCallback((url, config) => request('DELETE', url, null, config), [request]);

  /**
   * Reset des états
   */
  const reset = useCallback(() => {
    setData(null);
    setError(null);
    setLoading(false);
  }, []);

  return {
    data,
    loading,
    error,
    request,
    get,
    post,
    put,
    patch,
    delete: del,
    reset
  };
};

/**
 * Export de l'instance axios pour une utilisation directe si nécessaire
 */
export { apiClient };

export default useApi;
