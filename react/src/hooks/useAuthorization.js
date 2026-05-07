import { useState, useCallback } from 'react';

/**
 * Hook d'autorisation pour contrôler l'accès aux fonctionnalités
 * Prépare une méthode pour afficher "Not Authorized" si les conditions ne sont pas réunies
 * Actuellement, laisse toujours passer (à implémenter plus tard)
 */
const useAuthorization = () => {
  const [authError, setAuthError] = useState(null);
  const [isAuthorized, setIsAuthorized] = useState(true);

  /**
   * Vérifie si l'utilisateur a l'autorisation d'accéder à une ressource
   * @param {string} resource - La ressource à accéder (ex: 'dashboard', 'admin', etc.)
   * @param {object} conditions - Conditions à vérifier (à implémenter)
   * @returns {boolean} - true si autorisé, false sinon
   */
  const checkAuthorization = useCallback((resource, conditions = {}) => {
    // TODO: Implémenter la logique d'autorisation réelle
    // Pour l'instant, on laisse toujours passer
    const authorized = true;

    if (!authorized) {
      setAuthError({
        message: 'Not Authorized',
        resource,
        timestamp: new Date().toISOString()
      });
      setIsAuthorized(false);
      return false;
    }

    setAuthError(null);
    setIsAuthorized(true);
    return true;
  }, []);

  /**
   * Affiche une erreur d'autorisation
   * @param {string} message - Message d'erreur personnalisé
   */
  const showAuthError = useCallback((message = 'Not Authorized') => {
    setAuthError({
      message,
      timestamp: new Date().toISOString()
    });
    setIsAuthorized(false);
  }, []);

  /**
   * Efface l'erreur d'autorisation
   */
  const clearAuthError = useCallback(() => {
    setAuthError(null);
    setIsAuthorized(true);
  }, []);

  /**
   * Wrapper pour exécuter une action seulement si autorisé
   * @param {string} resource - Ressource à vérifier
   * @param {function} action - Action à exécuter si autorisé
   * @param {function} onUnauthorized - Callback si non autorisé (optionnel)
   */
  const withAuthorization = useCallback((resource, action, onUnauthorized) => {
    return (...args) => {
      if (checkAuthorization(resource)) {
        return action(...args);
      } else if (onUnauthorized) {
        onUnauthorized();
      }
    };
  }, [checkAuthorization]);

  return {
    isAuthorized,
    authError,
    checkAuthorization,
    showAuthError,
    clearAuthError,
    withAuthorization
  };
};

export default useAuthorization;
