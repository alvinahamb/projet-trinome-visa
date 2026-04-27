/**
 * Fonction fetch généralisée avec gestion des erreurs
 * @param {string} url - L'URL de l'API
 * @param {Object} options - Les options de la requête
 * @param {string} options.method - La méthode HTTP (GET, POST, PUT, DELETE, etc.)
 * @param {Object} options.body - Le corps de la requête (sera converti en JSON)
 * @param {Object} options.headers - Les en-têtes personnalisés
 * @param {number} options.timeout - Délai d'expiration en ms (par défaut 30000)
 * @returns {Promise<Object>} - La réponse de l'API au format {code, status, data, message, error}
 */
async function fetchApi(url, options = {}) {
    const {
        method = 'GET',
        body = null,
        headers = {},
        timeout = 30000
    } = options;

    // En-têtes par défaut
    const defaultHeaders = {
        'Content-Type': 'application/json',
        ...headers
    };

    // Créer le controller d'abort pour le timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);

    try {
        // Préparer les options de la requête
        const fetchOptions = {
            method,
            headers: defaultHeaders,
            signal: controller.signal
        };

        // Ajouter le body si présent
        if (body) {
            fetchOptions.body = JSON.stringify(body);
        }

        // Faire la requête
        const response = await fetch(url, fetchOptions);

        // Attendre la réponse JSON
        const responseData = await response.json();

        // Vérifier si la réponse est valide
        if (!response.ok) {
            console.error('Erreur API:', responseData);
            return {
                code: responseData.code || response.status,
                status: 'error',
                data: null,
                message: responseData.message || 'Une erreur est survenue',
                error: responseData.error || response.statusText
            };
        }

        return responseData;

    } catch (error) {
        // Gestion des erreurs réseau et autres erreurs
        let errorMessage = 'Une erreur est survenue';
        let errorCode = 500;

        if (error.name === 'AbortError') {
            errorMessage = `Timeout: La requête a dépassé le délai de ${timeout}ms`;
            errorCode = 408;
        } else if (error instanceof TypeError) {
            errorMessage = 'Erreur réseau: Vérifiez votre connexion';
            errorCode = 0;
        } else {
            errorMessage = error.message || errorMessage;
        }

        console.error('Erreur fetchApi:', error);

        return {
            code: errorCode,
            status: 'error',
            data: null,
            message: errorMessage,
            error: errorMessage
        };

    } finally {
        // Nettoyer le timeout
        clearTimeout(timeoutId);
    }
}

/**
 * Fonction GET
 * @param {string} url - L'URL
 * @param {Object} options - Options supplémentaires
 * @returns {Promise<Object>}
 */
function getApi(url, options = {}) {
    return fetchApi(url, { ...options, method: 'GET' });
}

/**
 * Fonction POST
 * @param {string} url - L'URL
 * @param {Object} body - Le corps de la requête
 * @param {Object} options - Options supplémentaires
 * @returns {Promise<Object>}
 */
function postApi(url, body, options = {}) {
    return fetchApi(url, { ...options, method: 'POST', body });
}

/**
 * Fonction PUT
 * @param {string} url - L'URL
 * @param {Object} body - Le corps de la requête
 * @param {Object} options - Options supplémentaires
 * @returns {Promise<Object>}
 */
function putApi(url, body, options = {}) {
    return fetchApi(url, { ...options, method: 'PUT', body });
}

/**
 * Fonction DELETE
 * @param {string} url - L'URL
 * @param {Object} options - Options supplémentaires
 * @returns {Promise<Object>}
 */
function deleteApi(url, options = {}) {
    return fetchApi(url, { ...options, method: 'DELETE' });
}

/**
 * Fonction pour traiter la réponse API
 * @param {Object} response - La réponse de l'API
 * @param {Function} onSuccess - Callback en cas de succès
 * @param {Function} onError - Callback en cas d'erreur
 */
function handleApiResponse(response, onSuccess, onError) {
    if (response.status === 'success') {
        if (typeof onSuccess === 'function') {
            onSuccess(response.data, response);
        }
    } else {
        if (typeof onError === 'function') {
            onError(response.error, response);
        } else {
            console.error('Erreur API:', response.error);
        }
    }
}
