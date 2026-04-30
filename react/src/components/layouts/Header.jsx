import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '@components/layouts/Navbar';
import Bouton from '@components/commons/Bouton';
import colors from '@assets/color/color';
import { authAPI } from '@api/auth.api';

/**
 * Header - En-tête avec logo+titre à gauche, navbar au centre, icône entreprise + bouton déconnexion à droite
 * Espacement symétrique et navbar avec scroll horizontal si nécessaire
 */
const Header = ({ 
  title = '', 
  navItems = [],
  showLogout = true
}) => {
  const navigate = useNavigate();
  const [showCompanyPopover, setShowCompanyPopover] = useState(false);
  const [entitesLegales, setEntitesLegales] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [isChanging, setIsChanging] = useState(false);
  const popoverRef = useRef(null);

  // Récupérer l'utilisateur connecté et charger les entités légales
  useEffect(() => {
    const user = authAPI.getUser();
    if (user) {
      setSelectedCompany(user.idEntiteLegale);
    }

    const fetchEntitesLegales = async () => {
      try {
        const entites = await authAPI.getEntitesLegales();
        setEntitesLegales(entites || []);
      } catch (err) {
        console.error('Erreur chargement entités:', err);
      }
    };

    if (authAPI.isAuthenticated()) {
      fetchEntitesLegales();
    }
  }, []);

  // Fermer le popover si clic extérieur
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (popoverRef.current && !popoverRef.current.contains(event.target)) {
        setShowCompanyPopover(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogoClick = () => {
    navigate('/menu');
  };

  const handleCompanyChange = async () => {
    if (!selectedCompany || isChanging) return;
    
    setIsChanging(true);
    try {
      await authAPI.updateEntiteLegale(selectedCompany);
      setShowCompanyPopover(false);
      // Recharger la page pour appliquer les changements
      window.location.reload();
    } catch (err) {
      console.error('Erreur changement entreprise:', err);
    } finally {
      setIsChanging(false);
    }
  };

  const handleLogout = async () => {
    try {
      await authAPI.logout();
      navigate('/');
    } catch (err) {
      console.error('Erreur déconnexion:', err);
      navigate('/');
    }
  };

  const headerStyle = {
    backgroundColor: colors.white,
    boxShadow: '0 2px 20px rgba(0, 0, 0, 0.06)',
    fontFamily: "'EB Garamond', Georgia, serif",
    position: 'sticky',
    top: 0,
    zIndex: 100
  };

  const containerStyle = {
    maxWidth: '1600px',
    margin: '0 auto',
    padding: '1rem 2rem',
    display: 'grid',
    gridTemplateColumns: '1fr auto 1fr',
    alignItems: 'center',
    gap: '2rem'
  };

  // Section gauche : Logo + Titre
  const leftSectionStyle = {
    display: 'flex',
    alignItems: 'center',
    gap: '1.5rem',
    justifyContent: 'flex-start'
  };

  const logoContainerStyle = {
    display: 'flex',
    alignItems: 'center',
    cursor: 'pointer',
    transition: 'transform 0.2s ease'
  };

  const logoImageStyle = {
    height: '42px',
    width: 'auto',
    objectFit: 'contain',
    borderRadius: '8px'
  };

  const titleStyle = {
    fontSize: '1.35rem',
    fontWeight: 700,
    color: colors.primary,
    letterSpacing: '-0.01em',
    margin: 0,
    whiteSpace: 'nowrap'
  };

  // Section centrale : NavBar (toujours centré)
  const centerSectionStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden',
    minWidth: 0
  };

  const navScrollContainerStyle = {
    overflowX: 'auto',
    overflowY: 'hidden',
    scrollBehavior: 'smooth',
    maxWidth: '100%',
    padding: '0.25rem 0',
    scrollbarWidth: 'thin',
    scrollbarColor: `${colors.primary} transparent`
  };

  // Section droite : Icône Dashboard + Icône entreprise + Bouton déconnexion
  const rightSectionStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    gap: '1.5rem'
  };

  // Style icône Dashboard
  const dashboardIconButtonStyle = {
    width: '40px',
    height: '40px',
    borderRadius: '10px',
    border: `2px solid ${colors.primary}`,
    backgroundColor: colors.white,
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    transition: 'all 0.3s ease',
    position: 'relative'
  };

  // Style icône entreprise (FontAwesome style)
  const companyIconButtonStyle = {
    width: '40px',
    height: '40px',
    borderRadius: '10px',
    border: `2px solid ${colors.action}`,
    backgroundColor: colors.white,
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    transition: 'all 0.3s ease',
    position: 'relative'
  };

  // Style du popover flottant
  const popoverStyle = {
    position: 'absolute',
    top: '100%',
    right: 0,
    marginTop: '0.75rem',
    backgroundColor: colors.white,
    borderRadius: '16px',
    boxShadow: '0 10px 40px rgba(0, 0, 0, 0.15)',
    border: `1px solid ${colors.gray.light}`,
    padding: '1.5rem',
    minWidth: '280px',
    zIndex: 1000,
    animation: 'fadeInDown 0.25s ease-out'
  };

  const popoverTitleStyle = {
    fontSize: '1.1rem',
    fontWeight: 700,
    color: colors.text.primary,
    marginBottom: '1rem',
    textAlign: 'center'
  };

  const companiesGridStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.75rem',
    marginBottom: '1.25rem'
  };

  const companyCardStyle = (isSelected, companyColor) => ({
    display: 'flex',
    alignItems: 'center',
    gap: '1rem',
    padding: '0.875rem 1rem',
    borderRadius: '12px',
    border: `2px solid ${isSelected ? companyColor : colors.gray.medium}`,
    backgroundColor: isSelected ? `${companyColor}10` : colors.white,
    cursor: 'pointer',
    transition: 'all 0.3s ease'
  });

  const companyIconStyle = {
    fontSize: '1.5rem'
  };

  const companyNameStyle = (isSelected) => ({
    fontSize: '0.95rem',
    fontWeight: isSelected ? 600 : 500,
    color: colors.text.primary
  });

  const LogoutIcon = () => (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
      <polyline points="16 17 21 12 16 7"></polyline>
      <line x1="21" y1="12" x2="9" y2="12"></line>
    </svg>
  );

  // Icône Building (FontAwesome style)
  const BuildingIcon = () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke={colors.action} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="4" y="2" width="16" height="20" rx="2" ry="2"></rect>
      <path d="M9 22v-4h6v4"></path>
      <path d="M8 6h.01"></path>
      <path d="M16 6h.01"></path>
      <path d="M12 6h.01"></path>
      <path d="M12 10h.01"></path>
      <path d="M12 14h.01"></path>
      <path d="M16 10h.01"></path>
      <path d="M16 14h.01"></path>
      <path d="M8 10h.01"></path>
      <path d="M8 14h.01"></path>
    </svg>
  );

  // Icône Dashboard (tableau de bord)
  const DashboardIcon = () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke={colors.primary} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="3" width="7" height="7"></rect>
      <rect x="14" y="3" width="7" height="7"></rect>
      <rect x="14" y="14" width="7" height="7"></rect>
      <rect x="3" y="14" width="7" height="7"></rect>
    </svg>
  );

  // Handler pour navigation Dashboard
  const handleDashboardClick = () => {
    navigate('/dashboard');
  };

  // CSS animations
  const animationCSS = `
    .nav-scroll-container::-webkit-scrollbar {
      height: 4px;
    }
    .nav-scroll-container::-webkit-scrollbar-track {
      background: transparent;
    }
    .nav-scroll-container::-webkit-scrollbar-thumb {
      background: ${colors.primary}40;
      border-radius: 4px;
    }
    .nav-scroll-container::-webkit-scrollbar-thumb:hover {
      background: ${colors.primary};
    }
    @keyframes fadeInDown {
      0% {
        opacity: 0;
        transform: translateY(-10px);
      }
      100% {
        opacity: 1;
        transform: translateY(0);
      }
    }
  `;

  return (
    <header style={headerStyle}>
      <style>{animationCSS}</style>
      <div style={containerStyle}>
        {/* Section Gauche : Logo + Titre */}
        <div style={leftSectionStyle}>
          <div 
            style={logoContainerStyle} 
            onClick={handleLogoClick}
            onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
            onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
            title="Retour au menu"
          >
            <img src="/logo.png" alt="Logo" style={logoImageStyle} />
          </div>
          {title && <h1 style={titleStyle}>{title}</h1>}
        </div>

        {/* Section Centrale : NavBar (toujours centré) */}
        <div style={centerSectionStyle}>
          {navItems.length > 0 && (
            <div className="nav-scroll-container" style={navScrollContainerStyle}>
              <NavBar navItems={navItems} />
            </div>
          )}
        </div>

        {/* Section Droite : Icône Dashboard + Icône Entreprise + Bouton déconnexion */}
        <div style={rightSectionStyle}>
          {/* Icône Dashboard */}
          <div
            style={dashboardIconButtonStyle}
            onClick={handleDashboardClick}
            onMouseEnter={(e) => {
              e.currentTarget.style.backgroundColor = `${colors.primary}10`;
              e.currentTarget.style.transform = 'scale(1.05)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.backgroundColor = colors.white;
              e.currentTarget.style.transform = 'scale(1)';
            }}
            title="Dashboard"
          >
            <DashboardIcon />
          </div>

          {/* Icône Entreprise avec Popover */}
          <div style={{ position: 'relative' }} ref={popoverRef}>
            <div
              style={companyIconButtonStyle}
              onClick={() => setShowCompanyPopover(!showCompanyPopover)}
              onMouseEnter={(e) => {
                e.currentTarget.style.backgroundColor = `${colors.action}10`;
                e.currentTarget.style.transform = 'scale(1.05)';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.backgroundColor = colors.white;
                e.currentTarget.style.transform = 'scale(1)';
              }}
              title="Changer d'entreprise"
            >
              <BuildingIcon />
            </div>

            {/* Popover flottant */}
            {showCompanyPopover && (
              <div style={popoverStyle}>
                <h3 style={popoverTitleStyle}>Changer d'entreprise</h3>
                
                <div style={companiesGridStyle}>
                  {entitesLegales.map((entite) => (
                    <div
                      key={entite.idEntiteLegale}
                      style={companyCardStyle(selectedCompany === entite.idEntiteLegale, colors.action)}
                      onClick={() => setSelectedCompany(entite.idEntiteLegale)}
                      onMouseEnter={(e) => {
                        if (selectedCompany !== entite.idEntiteLegale) {
                          e.currentTarget.style.borderColor = colors.action;
                          e.currentTarget.style.backgroundColor = `${colors.action}05`;
                        }
                      }}
                      onMouseLeave={(e) => {
                        if (selectedCompany !== entite.idEntiteLegale) {
                          e.currentTarget.style.borderColor = colors.gray.medium;
                          e.currentTarget.style.backgroundColor = colors.white;
                        }
                      }}
                    >
                      {entite.logo ? (
                        <img 
                          src={entite.logo} 
                          alt={entite.nom}
                          style={{ width: '32px', height: '32px', objectFit: 'contain', borderRadius: '4px' }}
                          onError={(e) => {
                            e.target.style.display = 'none';
                          }}
                        />
                      ) : (
                        <span style={companyIconStyle}>🏢</span>
                      )}
                      <div style={{ flex: 1 }}>
                        <span style={companyNameStyle(selectedCompany === entite.idEntiteLegale)}>
                          {entite.nom}
                        </span>
                        {entite.numero && (
                          <div style={{ fontSize: '0.75rem', color: colors.text.secondary }}>
                            {entite.numero}
                          </div>
                        )}
                      </div>
                    </div>
                  ))}
                </div>

                <Bouton 
                  size="sm" 
                  onClick={handleCompanyChange}
                  disabled={isChanging}
                  style={{ width: '100%' }}
                >
                  {isChanging ? 'Changement...' : 'Changer'}
                </Bouton>
              </div>
            )}
          </div>

          {/* Bouton Déconnexion */}
          {showLogout && (
            <Bouton 
              size="sm" 
              onClick={handleLogout}
            >
              <LogoutIcon />
              Déconnexion
            </Bouton>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
