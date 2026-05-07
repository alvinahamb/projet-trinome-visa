import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Bouton from '@components/commons/Bouton';
import colors from '@assets/color/color';
import { authAPI } from '@api/auth.api';

/**
 * Login - Page de connexion élégante et professionnelle
 */
const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    matricule: '',
    password: '' // Champ décoratif, non envoyé
  });
  const [selectedEntite, setSelectedEntite] = useState(null);
  const [entitesLegales, setEntitesLegales] = useState([]);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingEntites, setIsLoadingEntites] = useState(true);

  // Charger les entités légales au montage
  useEffect(() => {
    const fetchEntitesLegales = async () => {
      try {
        setIsLoadingEntites(true);
        const entites = await authAPI.getEntitesLegalesPublic();
        setEntitesLegales(entites || []);
      } catch (err) {
        console.error('Erreur chargement entités:', err);
        setError('Impossible de charger les entreprises');
      } finally {
        setIsLoadingEntites(false);
      }
    };

    fetchEntitesLegales();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError('');
  };

  const handleEntiteSelect = (idEntite) => {
    setSelectedEntite(idEntite);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validation
    if (!formData.matricule) {
      setError('Veuillez entrer votre matricule');
      return;
    }

    if (!selectedEntite) {
      setError('Veuillez sélectionner une entreprise');
      return;
    }

    setIsLoading(true);

    try {
      // Appel à l'API de login
      await authAPI.login(formData.matricule, selectedEntite);
      
      // Succès - rediriger vers le menu
      navigate('/menu');
    } catch (err) {
      setError(err.message || 'Erreur de connexion. Veuillez réessayer.');
    } finally {
      setIsLoading(false);
    }
  };

  const containerStyle = {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontFamily: "'EB Garamond', Georgia, serif",
    background: `linear-gradient(135deg, ${colors.background.main} 0%, ${colors.white} 40%, ${colors.gray.light} 100%)`,
    padding: '2rem',
    position: 'relative',
    overflow: 'hidden'
  };

  // Éléments décoratifs
  const decorStyle1 = {
    position: 'absolute',
    top: '-150px',
    right: '-150px',
    width: '400px',
    height: '400px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.primary}15 0%, transparent 70%)`,
    pointerEvents: 'none'
  };

  const decorStyle2 = {
    position: 'absolute',
    bottom: '-100px',
    left: '-100px',
    width: '300px',
    height: '300px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.action}10 0%, transparent 70%)`,
    pointerEvents: 'none'
  };

  const cardStyle = {
    backgroundColor: colors.white,
    borderRadius: '24px',
    boxShadow: '0 20px 60px rgba(0, 0, 0, 0.1)',
    padding: '3rem',
    width: '100%',
    maxWidth: '450px',
    position: 'relative',
    zIndex: 1,
    animation: 'slideUp 0.6s ease-out forwards'
  };

  const titleStyle = {
    fontSize: '2rem',
    fontWeight: 700,
    color: colors.text.primary,
    textAlign: 'center',
    marginBottom: '0.5rem'
  };

  const formStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '1.5rem'
  };

  const inputGroupStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.5rem'
  };

  const labelStyle = {
    fontSize: '0.95rem',
    fontWeight: 600,
    color: colors.text.primary
  };

  const inputStyle = {
    padding: '0.875rem 1rem',
    fontSize: '1rem',
    border: `2px solid ${colors.gray.medium}`,
    borderRadius: '12px',
    fontFamily: "'EB Garamond', Georgia, serif",
    transition: 'all 0.3s ease',
    backgroundColor: colors.background.main
  };

  const roleContainerStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.75rem'
  };

  const roleLabelStyle = {
    fontSize: '0.95rem',
    fontWeight: 600,
    color: colors.text.primary,
    marginBottom: '0.25rem'
  };

  const rolesGridStyle = {
    display: 'grid',
    gridTemplateColumns: entitesLegales.length > 3 ? 'repeat(3, 1fr)' : `repeat(${entitesLegales.length || 1}, 1fr)`,
    gap: '0.75rem'
  };

  const entiteCardStyle = (isSelected) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '1rem 0.5rem',
    borderRadius: '12px',
    border: `2px solid ${isSelected ? colors.action : colors.gray.medium}`,
    backgroundColor: isSelected ? `${colors.action}10` : colors.white,
    cursor: 'pointer',
    transition: 'all 0.3s ease',
    gap: '0.5rem'
  });

  const logoStyle = {
    width: '60px',
    height: '60px',
    objectFit: 'contain',
    borderRadius: '8px'
  };

  const entiteTextStyle = (isSelected) => ({
    fontSize: '0.8rem',
    fontWeight: isSelected ? 600 : 500,
    color: colors.text.primary,
    textAlign: 'center'
  });

  const errorStyle = {
    backgroundColor: `${colors.error}10`,
    border: `1px solid ${colors.error}30`,
    borderRadius: '12px',
    padding: '1rem',
    color: colors.error,
    fontSize: '0.95rem',
    textAlign: 'center',
    animation: 'shake 0.5s ease-in-out'
  };

  const keyframesStyle = `
    @keyframes slideUp {
      0% {
        opacity: 0;
        transform: translateY(30px);
      }
      100% {
        opacity: 1;
        transform: translateY(0);
      }
    }
    
    @keyframes shake {
      0%, 100% { transform: translateX(0); }
      20%, 60% { transform: translateX(-5px); }
      40%, 80% { transform: translateX(5px); }
    }
    
    input:focus {
      border-color: ${colors.primary} !important;
      box-shadow: 0 0 0 3px ${colors.primary}20 !important;
      outline: none;
    }
  `;

  return (
    <div style={containerStyle}>
      <style>{keyframesStyle}</style>
      
      {/* Éléments décoratifs */}
      <div style={decorStyle1}></div>
      <div style={decorStyle2}></div>

      <div style={cardStyle}>
        {/* Logo */}
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '1rem' }}>
          <img 
            src="/logo.png" 
            alt="Logo" 
            style={{ 
              height: '80px', 
              width: 'auto', 
              objectFit: 'contain',
              borderRadius: '12px'
            }} 
          />
        </div>
        
        {/* Titre */}
        <h1 style={titleStyle}>Bienvenue</h1>

        <form style={formStyle} onSubmit={handleSubmit}>
          {/* Champ Matricule */}
          <div style={inputGroupStyle}>
            <label style={labelStyle} htmlFor="matricule">
              Matricule
            </label>
            <input
              type="text"
              id="matricule"
              name="matricule"
              value={formData.matricule}
              onChange={handleInputChange}
              placeholder="EMPXXXXX"
              style={inputStyle}
              onFocus={(e) => {
                e.target.style.borderColor = colors.primary;
                e.target.style.boxShadow = `0 0 0 3px ${colors.primary}20`;
              }}
              onBlur={(e) => {
                e.target.style.borderColor = colors.gray.medium;
                e.target.style.boxShadow = 'none';
              }}
            />
          </div>

          {/* Champ Mot de passe (décoratif) */}
          <div style={inputGroupStyle}>
            <label style={labelStyle} htmlFor="password">
              Mot de passe
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="••••••••"
              style={inputStyle}
              onFocus={(e) => {
                e.target.style.borderColor = colors.primary;
                e.target.style.boxShadow = `0 0 0 3px ${colors.primary}20`;
              }}
              onBlur={(e) => {
                e.target.style.borderColor = colors.gray.medium;
                e.target.style.boxShadow = 'none';
              }}
            />
          </div>

          {/* Sélection de l'entité légale (images cliquables) */}
          <div style={roleContainerStyle}>
            <span style={roleLabelStyle}>Choisir l'entreprise</span>
            {isLoadingEntites ? (
              <div style={{ textAlign: 'center', padding: '1rem', color: colors.text.secondary }}>
                Chargement des entreprises...
              </div>
            ) : entitesLegales.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '1rem', color: colors.text.secondary }}>
                Aucune entreprise disponible
              </div>
            ) : (
              <div style={rolesGridStyle}>
                {entitesLegales.map((entite) => (
                  <div
                    key={entite.idEntiteLegale}
                    style={entiteCardStyle(selectedEntite === entite.idEntiteLegale)}
                    onClick={() => handleEntiteSelect(entite.idEntiteLegale)}
                    onMouseEnter={(e) => {
                      if (selectedEntite !== entite.idEntiteLegale) {
                        e.currentTarget.style.borderColor = colors.action;
                        e.currentTarget.style.transform = 'translateY(-2px)';
                      }
                    }}
                    onMouseLeave={(e) => {
                      if (selectedEntite !== entite.idEntiteLegale) {
                        e.currentTarget.style.borderColor = colors.gray.medium;
                        e.currentTarget.style.transform = 'translateY(0)';
                      }
                    }}
                  >
                    {entite.logo ? (
                      <img 
                        src={entite.logo} 
                        alt={entite.nom}
                        style={logoStyle}
                        onError={(e) => {
                          e.target.style.display = 'none';
                          e.target.nextSibling.style.display = 'flex';
                        }}
                      />
                    ) : null}
                    <div 
                      style={{ 
                        ...logoStyle, 
                        display: entite.logo ? 'none' : 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        backgroundColor: colors.gray.light,
                        fontSize: '1.5rem'
                      }}
                    >
                      🏢
                    </div>
                    <span style={entiteTextStyle(selectedEntite === entite.idEntiteLegale)}>
                      {entite.nom}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Message d'erreur */}
          {error && (
            <div style={errorStyle}>
              ⚠️ {error}
            </div>
          )}

          {/* Bouton de connexion */}
          <Bouton
            type="submit"
            variant="action"
            size="lg"
            disabled={isLoading}
            style={{ width: '100%', marginTop: '0.5rem' }}
          >
            {isLoading ? (
              <span style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <svg 
                  width="20" 
                  height="20" 
                  viewBox="0 0 24 24" 
                  style={{ animation: 'spin 1s linear infinite' }}
                >
                  <circle 
                    cx="12" 
                    cy="12" 
                    r="10" 
                    stroke="currentColor" 
                    strokeWidth="3" 
                    fill="none" 
                    strokeDasharray="30 70"
                  />
                </svg>
                Connexion en cours...
              </span>
            ) : (
              'Se Connecter'
            )}
          </Bouton>
        </form>
      </div>

      <style>{`
        @keyframes spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

export default Login;