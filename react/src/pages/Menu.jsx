import React from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '@components/layouts/Layout';
import colors from '@assets/color/color';

/**
 * Menu - Page de menu principal avec Layout intégré
 */
const Menu = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Logique de déconnexion
    navigate('/login');
  };

  // Icônes FontAwesome style
  const ShoppingCartIcon = ({ color }) => (
    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="9" cy="21" r="1"></circle>
      <circle cx="20" cy="21" r="1"></circle>
      <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
    </svg>
  );

  const DollarSignIcon = ({ color }) => (
    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="12" y1="1" x2="12" y2="23"></line>
      <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
    </svg>
  );

  const PackageIcon = ({ color }) => (
    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="16.5" y1="9.4" x2="7.5" y2="4.21"></line>
      <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
      <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
      <line x1="12" y1="22.08" x2="12" y2="12"></line>
    </svg>
  );

  // Options du menu principal
  const menuOptions = [
    {
      id: 'achat',
      title: 'Achats',
      description: 'Gérer les commandes, l\'approvisionnement et les fournisseurs',
      icon: ShoppingCartIcon,
      to: '/achat',
      color: colors.action
    },
    {
      id: 'vente',
      title: 'Ventes',
      description: 'Suivre les reservations, la commercialisation et les clients',
      icon: DollarSignIcon,
      to: '/vente',
      color: colors.primary
    },
    {
      id: 'stock',
      title: 'Stock',
      description: 'Controler l\'inventaire, mouvements et valeur de stock',
      icon: PackageIcon,
      to: '/stock',
      color: '#9B59B6'
    }
  ];

  const containerStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '3rem',
    padding: '3rem 2rem',
    minHeight: 'calc(100vh - 200px)',
    position: 'relative',
    overflow: 'hidden'
  };

  // Background design moderne et épuré
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
    top: '-10%',
    right: '-5%',
    width: '500px',
    height: '500px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.primary}08 0%, transparent 70%)`,
    filter: 'blur(60px)',
    animation: 'float 20s ease-in-out infinite'
  };

  const shape2Style = {
    position: 'absolute',
    bottom: '-15%',
    left: '-8%',
    width: '600px',
    height: '600px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.action}06 0%, transparent 70%)`,
    filter: 'blur(70px)',
    animation: 'float 25s ease-in-out infinite reverse'
  };

  const shape3Style = {
    position: 'absolute',
    top: '30%',
    left: '10%',
    width: '300px',
    height: '300px',
    borderRadius: '30% 70% 70% 30% / 30% 30% 70% 70%',
    background: `linear-gradient(135deg, ${colors.primary}05 0%, transparent 100%)`,
    filter: 'blur(50px)',
    animation: 'morph 15s ease-in-out infinite'
  };

  const shape4Style = {
    position: 'absolute',
    bottom: '20%',
    right: '15%',
    width: '400px',
    height: '400px',
    borderRadius: '60% 40% 30% 70% / 60% 30% 70% 40%',
    background: `linear-gradient(225deg, #9B59B605 0%, transparent 100%)`,
    filter: 'blur(55px)',
    animation: 'morph 18s ease-in-out infinite reverse'
  };

  // Lignes courbes décoratives
  const curveLineStyle = {
    position: 'absolute',
    top: '15%',
    right: '20%',
    width: '350px',
    height: '250px',
    opacity: 0.03
  };

  const menuGridStyle = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignItems: 'stretch',
    gap: '2.5rem',
    width: '100%',
    maxWidth: '1100px',
    position: 'relative',
    zIndex: 1
  };

  const menuCardStyle = (cardColor) => ({
    backgroundColor: colors.white,
    borderRadius: '20px',
    padding: '2rem',
    boxShadow: '0 10px 40px rgba(0, 0, 0, 0.08)',
    border: `1px solid ${colors.gray.light}`,
    transition: 'all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    cursor: 'pointer',
    position: 'relative',
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column',
    gap: '1.25rem',
    width: '300px',
    minHeight: '280px',
    flex: '0 0 auto'
  });

  const cardAccentStyle = (cardColor) => ({
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: '4px',
    backgroundColor: cardColor,
    transition: 'height 0.3s ease'
  });

  const iconContainerStyle = (cardColor) => ({
    width: '60px',
    height: '60px',
    borderRadius: '16px',
    backgroundColor: `${cardColor}15`,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '1.75rem',
    transition: 'all 0.3s ease'
  });

  const cardTitleStyle = {
    fontSize: '1.5rem',
    fontWeight: 600,
    color: colors.text.primary
  };

  const cardDescriptionStyle = {
    fontSize: '1rem',
    color: colors.text.secondary,
    lineHeight: 1.5
  };

  const cardArrowStyle = (cardColor) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    marginTop: 'auto',
    color: cardColor,
    fontWeight: 600,
    fontSize: '0.95rem',
    gap: '0.5rem',
    transition: 'transform 0.3s ease'
  });

  const keyframesStyle = `
    @keyframes fadeIn {
      0% { opacity: 0; transform: translateY(20px); }
      100% { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes float {
      0%, 100% { transform: translate(0, 0) scale(1); }
      33% { transform: translate(30px, -30px) scale(1.1); }
      66% { transform: translate(-20px, 20px) scale(0.9); }
    }
    
    @keyframes morph {
      0%, 100% { 
        border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%;
        transform: rotate(0deg);
      }
      50% { 
        border-radius: 30% 60% 70% 40% / 50% 60% 30% 60%;
        transform: rotate(180deg);
      }
    }
  `;
const menuNavItems = [
  {
    label: 'Achats',
    to: '/achat',
    badge: ''
  },
  {
    label: 'Ventes',
    to: '/vente',
    badge: ''
  },
  {
    label: 'Stock',
    to: '/stock',
    badge: ''
  }
];

  return (
    <Layout 
      title="Menu Principal"
      navItems={menuNavItems}
      onLogout={handleLogout}
      showLogout={true}
    >
      <style>{keyframesStyle}</style>
      
      <div style={containerStyle}>
        {/* Background design moderne avec formes géométriques */}
        <div style={backgroundShapesStyle}>
          {/* Cercles flous */}
          <div style={shape1Style}></div>
          <div style={shape2Style}></div>
          
          {/* Formes organiques morphing */}
          <div style={shape3Style}></div>
          <div style={shape4Style}></div>
          
          {/* Lignes courbes décoratives SVG */}
          <svg style={curveLineStyle} viewBox="0 0 350 250" fill="none">
            <path d="M0 125 Q87.5 50, 175 125 T350 125" stroke={colors.primary} strokeWidth="2" opacity="0.15"/>
            <path d="M0 150 Q87.5 75, 175 150 T350 150" stroke={colors.action} strokeWidth="1.5" opacity="0.1"/>
          </svg>
          
          <svg style={{ ...curveLineStyle, top: '60%', left: '10%', right: 'auto' }} viewBox="0 0 300 200" fill="none">
            <circle cx="50" cy="50" r="40" stroke={colors.primary} strokeWidth="1" opacity="0.08" fill="none"/>
            <circle cx="150" cy="100" r="60" stroke="#9B59B6" strokeWidth="1" opacity="0.06" fill="none"/>
            <circle cx="250" cy="150" r="45" stroke={colors.action} strokeWidth="1" opacity="0.07" fill="none"/>
          </svg>
        </div>

        {/* Grille des options */}
        <div style={menuGridStyle}>
          {menuOptions.map((option, index) => (
            <div
              key={option.id}
              style={{
                ...menuCardStyle(option.color),
                animationDelay: `${index * 0.15}s`,
                animation: 'fadeIn 0.6s ease-out forwards',
                opacity: 0
              }}
              onClick={() => navigate(option.to)}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = 'translateY(-8px)';
                e.currentTarget.style.boxShadow = '0 20px 60px rgba(0, 0, 0, 0.15)';
                const accent = e.currentTarget.querySelector('.card-accent');
                if (accent) accent.style.height = '6px';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = 'translateY(0)';
                e.currentTarget.style.boxShadow = '0 10px 40px rgba(0, 0, 0, 0.08)';
                const accent = e.currentTarget.querySelector('.card-accent');
                if (accent) accent.style.height = '4px';
              }}
            >
              <div className="card-accent" style={cardAccentStyle(option.color)}></div>
              
              <div style={iconContainerStyle(option.color)}>
                <option.icon color={option.color} />
              </div>
              
              <h3 style={cardTitleStyle}>{option.title}</h3>
              <p style={cardDescriptionStyle}>{option.description}</p>
              
              <div style={cardArrowStyle(option.color)}>
                Accéder
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
              </div>
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
};

export default Menu;