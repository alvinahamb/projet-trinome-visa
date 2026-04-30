import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import colors from '@assets/color/color';
import useAuthorization from '@/hooks/useAuthorization';

/**
 * Dashboard - Page avec sidebar dépliable et design moderne
 */
const Dashboard = () => {
  const navigate = useNavigate();
  const [sidebarExpanded, setSidebarExpanded] = useState(false);
  const { authError, clearAuthError } = useAuthorization();

  // Items du sidebar
  const sidebarItems = [
    { id: 'overview', label: 'Vue d\'ensemble', icon: 'grid' },
    { id: 'analytics', label: 'Analytiques', icon: 'chart' },
    { id: 'reports', label: 'Rapports', icon: 'file' },
    { id: 'settings', label: 'Paramètres', icon: 'settings' }
  ];

  // Icônes SVG
  const icons = {
    grid: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="3" y="3" width="7" height="7"></rect>
        <rect x="14" y="3" width="7" height="7"></rect>
        <rect x="14" y="14" width="7" height="7"></rect>
        <rect x="3" y="14" width="7" height="7"></rect>
      </svg>
    ),
    chart: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <line x1="18" y1="20" x2="18" y2="10"></line>
        <line x1="12" y1="20" x2="12" y2="4"></line>
        <line x1="6" y1="20" x2="6" y2="14"></line>
      </svg>
    ),
    file: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
        <polyline points="14 2 14 8 20 8"></polyline>
        <line x1="16" y1="13" x2="8" y2="13"></line>
        <line x1="16" y1="17" x2="8" y2="17"></line>
      </svg>
    ),
    settings: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="3"></circle>
        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
      </svg>
    )
  };

  // Styles
  const pageContainerStyle = {
    display: 'flex',
    minHeight: '100vh',
    fontFamily: "'EB Garamond', Georgia, serif",
    backgroundColor: colors.background.main,
    position: 'relative',
    overflow: 'hidden'
  };

  // Sidebar styles
  const sidebarStyle = {
    width: sidebarExpanded ? '240px' : '70px',
    backgroundColor: colors.white,
    boxShadow: '2px 0 20px rgba(0, 0, 0, 0.06)',
    display: 'flex',
    flexDirection: 'column',
    transition: 'width 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    zIndex: 50,
    position: 'relative'
  };

  const logoContainerStyle = {
    padding: '1.5rem',
    display: 'flex',
    alignItems: 'center',
    justifyContent: sidebarExpanded ? 'flex-start' : 'center',
    gap: '1rem',
    borderBottom: `1px solid ${colors.gray.light}`,
    cursor: 'pointer',
    transition: 'all 0.3s ease'
  };

  const logoImageStyle = {
    height: '50px',
    width: 'auto',
    objectFit: 'contain',
    borderRadius: '10px',
    transition: 'transform 0.2s ease'
  };

  const sidebarNavStyle = {
    flex: 1,
    padding: '1rem 0.75rem',
    display: 'flex',
    flexDirection: 'column',
    gap: '0.5rem'
  };

  const sidebarItemStyle = (isActive) => ({
    display: 'flex',
    alignItems: 'center',
    gap: '1rem',
    padding: '0.875rem 1rem',
    borderRadius: '12px',
    cursor: 'pointer',
    transition: 'all 0.3s ease',
    backgroundColor: isActive ? `${colors.primary}15` : 'transparent',
    color: isActive ? colors.primary : colors.text.secondary,
    whiteSpace: 'nowrap',
    overflow: 'hidden'
  });

  const sidebarLabelStyle = {
    fontSize: '0.95rem',
    fontWeight: 500,
    opacity: sidebarExpanded ? 1 : 0,
    transition: 'opacity 0.2s ease',
    width: sidebarExpanded ? 'auto' : 0
  };

  // Main content styles
  const mainContentStyle = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    position: 'relative'
  };

  const contentAreaStyle = {
    flex: 1,
    padding: '3rem',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    position: 'relative'
  };

  // Background shapes
  const backgroundShapesStyle = {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    zIndex: 0,
    pointerEvents: 'none',
    overflow: 'hidden'
  };

  const shape1Style = {
    position: 'absolute',
    top: '10%',
    right: '5%',
    width: '400px',
    height: '400px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.primary}08 0%, transparent 70%)`,
    filter: 'blur(60px)'
  };

  const shape2Style = {
    position: 'absolute',
    bottom: '10%',
    left: '10%',
    width: '500px',
    height: '500px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.action}06 0%, transparent 70%)`,
    filter: 'blur(70px)'
  };

  // Title styles
  const titleContainerStyle = {
    textAlign: 'center',
    marginBottom: '3rem',
    position: 'relative',
    zIndex: 1
  };

  const mainTitleStyle = {
    fontSize: '2.75rem',
    fontWeight: 700,
    color: colors.text.primary,
    letterSpacing: '-0.02em',
    marginBottom: '0.5rem'
  };

  const subtitleStyle = {
    fontSize: '1.15rem',
    color: colors.primary,
    fontWeight: 500,
    letterSpacing: '0.1em',
    textTransform: 'uppercase'
  };

  const decorLineStyle = {
    width: '80px',
    height: '3px',
    backgroundColor: colors.action,
    margin: '1.5rem auto 0',
    borderRadius: '3px'
  };

  // Error modal styles
  const errorOverlayStyle = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1000
  };

  const errorModalStyle = {
    backgroundColor: colors.white,
    borderRadius: '20px',
    padding: '2.5rem',
    boxShadow: '0 20px 60px rgba(0, 0, 0, 0.2)',
    textAlign: 'center',
    maxWidth: '400px'
  };

  const errorIconStyle = {
    width: '60px',
    height: '60px',
    borderRadius: '50%',
    backgroundColor: `${colors.action}15`,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    margin: '0 auto 1.5rem'
  };

  const errorTitleStyle = {
    fontSize: '1.5rem',
    fontWeight: 700,
    color: colors.action,
    marginBottom: '0.75rem'
  };

  const errorMessageStyle = {
    fontSize: '1rem',
    color: colors.text.secondary,
    marginBottom: '1.5rem'
  };

  // CSS animations
  const animationCSS = `
    @keyframes fadeIn {
      0% { opacity: 0; transform: translateY(20px); }
      100% { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.6; }
    }
  `;

  return (
    <div style={pageContainerStyle}>
      <style>{animationCSS}</style>

      {/* Sidebar */}
      <aside 
        style={sidebarStyle}
        onMouseEnter={() => setSidebarExpanded(true)}
        onMouseLeave={() => setSidebarExpanded(false)}
      >
        {/* Logo */}
        <div 
          style={logoContainerStyle}
          onClick={() => navigate('/menu')}
          title="Menu"
          onMouseEnter={(e) => {
            const img = e.currentTarget.querySelector('img');
            if (img) img.style.transform = 'scale(1.08)';
          }}
          onMouseLeave={(e) => {
            const img = e.currentTarget.querySelector('img');
            if (img) img.style.transform = 'scale(1)';
          }}
        >
          <img src="/logo.png" alt="Logo" style={logoImageStyle} />
          {sidebarExpanded && (
            <span style={{ 
              fontSize: '1.1rem', 
              fontWeight: 600, 
              color: colors.primary,
              opacity: sidebarExpanded ? 1 : 0,
              transition: 'opacity 0.3s ease'
            }}>
              Menu
            </span>
          )}
        </div>

        {/* Navigation items */}
        <nav style={sidebarNavStyle}>
          {sidebarItems.map((item, index) => (
            <div
              key={item.id}
              style={sidebarItemStyle(index === 0)}
              onMouseEnter={(e) => {
                if (index !== 0) {
                  e.currentTarget.style.backgroundColor = colors.gray.light;
                }
              }}
              onMouseLeave={(e) => {
                if (index !== 0) {
                  e.currentTarget.style.backgroundColor = 'transparent';
                }
              }}
              title={!sidebarExpanded ? item.label : ''}
            >
              <span style={{ flexShrink: 0 }}>{icons[item.icon]}</span>
              <span style={sidebarLabelStyle}>{item.label}</span>
            </div>
          ))}
        </nav>
      </aside>

      {/* Main Content */}
      <main style={mainContentStyle}>
        <div style={contentAreaStyle}>
          {/* Background shapes */}
          <div style={backgroundShapesStyle}>
            <div style={shape1Style}></div>
            <div style={shape2Style}></div>
            
            {/* Decorative lines */}
            <svg style={{ position: 'absolute', top: '20%', left: '20%', opacity: 0.03 }} width="300" height="200" viewBox="0 0 300 200">
              <path d="M0 100 Q75 50, 150 100 T300 100" stroke={colors.primary} strokeWidth="2" fill="none"/>
              <path d="M0 130 Q75 80, 150 130 T300 130" stroke={colors.action} strokeWidth="1.5" fill="none"/>
            </svg>
            
            <svg style={{ position: 'absolute', bottom: '25%', right: '15%', opacity: 0.04 }} width="250" height="250" viewBox="0 0 250 250">
              <circle cx="125" cy="125" r="100" stroke={colors.primary} strokeWidth="1" fill="none"/>
              <circle cx="125" cy="125" r="70" stroke={colors.action} strokeWidth="1" fill="none"/>
              <circle cx="125" cy="125" r="40" stroke="#9B59B6" strokeWidth="1" fill="none"/>
            </svg>
          </div>

          {/* Title */}
          <div style={titleContainerStyle}>
            <h1 style={mainTitleStyle}>Dashboard</h1>
            <p style={subtitleStyle}>X-Port Enterprise</p>
            <div style={decorLineStyle}></div>
          </div>

          {/* Dashboard content placeholder */}
          <div style={{ 
            position: 'relative', 
            zIndex: 1, 
            width: '100%', 
            maxWidth: '1200px',
            animation: 'fadeIn 0.6s ease-out'
          }}>
            {/* Future dashboard widgets go here */}
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
              gap: '1.5rem'
            }}>
              {[1, 2, 3, 4].map((i) => (
                <div key={i} style={{
                  backgroundColor: colors.white,
                  borderRadius: '16px',
                  padding: '1.5rem',
                  boxShadow: '0 4px 20px rgba(0, 0, 0, 0.06)',
                  border: `1px solid ${colors.gray.light}`,
                  minHeight: '150px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: colors.text.secondary,
                  fontSize: '0.95rem'
                }}>
                  Widget {i}
                </div>
              ))}
            </div>
          </div>
        </div>
      </main>

      {/* Error Modal (if authorization fails) */}
      {authError && (
        <div style={errorOverlayStyle} onClick={clearAuthError}>
          <div style={errorModalStyle} onClick={(e) => e.stopPropagation()}>
            <div style={errorIconStyle}>
              <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke={colors.action} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="12" y1="8" x2="12" y2="12"></line>
                <line x1="12" y1="16" x2="12.01" y2="16"></line>
              </svg>
            </div>
            <h2 style={errorTitleStyle}>{authError.message}</h2>
            <p style={errorMessageStyle}>
              Vous n'avez pas les autorisations nécessaires pour accéder à cette ressource.
            </p>
            <button
              onClick={clearAuthError}
              style={{
                backgroundColor: colors.action,
                color: colors.white,
                border: 'none',
                padding: '0.75rem 2rem',
                borderRadius: '10px',
                fontSize: '1rem',
                fontWeight: 600,
                cursor: 'pointer',
                fontFamily: "'EB Garamond', Georgia, serif"
              }}
            >
              Fermer
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
