// Service d'authentification pour le frontend

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:5000/api';

/**
 * Service d'authentification
 */
export const authAPI = {
  /**
   * Connexion d'un employé
   * @param {string} numero - Numéro de l'employé (ex: "EMP00001")
   * @param {number} idEntiteLegale - ID de l'entité légale
   * @returns {Promise<Object>} Résultat de la connexion avec token et user
   */
  async login(numero, idEntiteLegale) {
    try {
      const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ numero, idEntiteLegale }),
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message || 'Erreur lors de la connexion');
      }

      // Sauvegarde du token et user dans le localStorage
      if (result.data?.token) {
        localStorage.setItem('token', result.data.token);
        localStorage.setItem('user', JSON.stringify(result.data.user));
      }

      return result;
    } catch (error) {
      console.error('Erreur login:', error);
      throw error;
    }
  },

  /**
   * Déconnexion
   */
  async logout() {
    try {
      const token = localStorage.getItem('token');

      if (token) {
        await fetch(`${API_URL}/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
      }
    } catch (error) {
      console.error('Erreur logout:', error);
    } finally {
      // Suppression des données locales dans tous les cas
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  },

  /**
   * Récupère toutes les entités légales
   * @returns {Promise<Array>} Liste des entités légales
   */
  async getEntitesLegales() {
    try {
      const token = localStorage.getItem('token');

      const response = await fetch(`${API_URL}/entite-legale`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message || 'Erreur lors de la récupération des entités légales');
      }

      return result.data.entitesLegales;
    } catch (error) {
      console.error('Erreur getEntitesLegales:', error);
      throw error;
    }
  },

  /**
   * Récupère toutes les entités légales (sans authentification pour le login)
   * @returns {Promise<Array>} Liste des entités légales
   */
  async getEntitesLegalesPublic() {
    try {
      // Pour le login, on fait un appel sans token
      // Le backend doit avoir une route publique ou on crée une route spéciale
      // Pour l'instant on utilise une route simple
      const response = await fetch(`${API_URL}/entite-legale/public`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message || 'Erreur lors de la récupération des entités légales');
      }

      return result.data.entitesLegales;
    } catch (error) {
      console.error('Erreur getEntitesLegalesPublic:', error);
      throw error;
    }
  },

  /**
   * Met à jour l'entité légale de la session actuelle
   * @param {number} idEntiteLegale - Nouvel ID de l'entité légale
   * @returns {Promise<Object>} Nouveau token et user
   */
  async updateEntiteLegale(idEntiteLegale) {
    try {
      const token = localStorage.getItem('token');

      const response = await fetch(`${API_URL}/entite-legale`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ idEntiteLegale }),
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message || 'Erreur lors de la mise à jour de l\'entité légale');
      }

      // Mise à jour du token et user dans le localStorage
      if (result.data?.token) {
        localStorage.setItem('token', result.data.token);
        localStorage.setItem('user', JSON.stringify(result.data.user));
      }

      return result;
    } catch (error) {
      console.error('Erreur updateEntiteLegale:', error);
      throw error;
    }
  },

  /**
   * Vérifie si l'utilisateur est connecté
   * @returns {boolean}
   */
  isAuthenticated() {
    return !!localStorage.getItem('token');
  },

  /**
   * Récupère le token du localStorage
   * @returns {string|null}
   */
  getToken() {
    return localStorage.getItem('token');
  },

  /**
   * Récupère les données utilisateur du localStorage
   * @returns {Object|null}
   */
  getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
};

/**
 * Intercepteur pour ajouter automatiquement le token aux requêtes
 */
export async function fetchWithAuth(url, options = {}) {
  const token = authAPI.getToken();

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  // Si non autorisé, déconnexion
  if (response.status === 401 || response.status === 403) {
    await authAPI.logout();
    window.location.href = '/';
  }

  return response;
}

export default authAPI;
  
