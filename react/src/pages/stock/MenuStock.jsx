import React from 'react';
import colors from '@assets/color/color';

/**
 * MenuStock - Page principale du module Stock
 */
const MenuStock = () => {
  const containerStyle = {
    fontFamily: "'EB Garamond', Georgia, serif"
  };

  const headerStyle = {
    marginBottom: '2rem'
  };

  const titleStyle = {
    fontSize: '1.75rem',
    fontWeight: 600,
    color: colors.text.primary,
    marginBottom: '0.5rem'
  };

  const subtitleStyle = {
    fontSize: '1rem',
    color: colors.text.secondary
  };

  const statsGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
    gap: '1.5rem',
    marginBottom: '2rem'
  };

  const statCardStyle = {
    backgroundColor: colors.white,
    borderRadius: '16px',
    padding: '1.5rem',
    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.06)',
    border: `1px solid ${colors.gray.light}`
  };

  const statValueStyle = {
    fontSize: '2rem',
    fontWeight: 700,
    color: '#9B59B6',
    marginBottom: '0.25rem'
  };

  const statLabelStyle = {
    fontSize: '0.9rem',
    color: colors.text.secondary
  };

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h2 style={titleStyle}>Bienvenue dans le module Stock</h2>
        <p style={subtitleStyle}>Gérez votre inventaire et les mouvements de stock</p>
      </div>

      <div style={statsGridStyle}>
        <div style={statCardStyle}>
          <div style={statValueStyle}>0</div>
          <div style={statLabelStyle}>Articles en stock</div>
        </div>
        <div style={statCardStyle}>
          <div style={{ ...statValueStyle, color: colors.action }}>0</div>
          <div style={statLabelStyle}>Alertes de stock bas</div>
        </div>
        <div style={statCardStyle}>
          <div style={{ ...statValueStyle, color: colors.primary }}>0</div>
          <div style={statLabelStyle}>Mouvements du jour</div>
        </div>
      </div>
    </div>
  );
};

export default MenuStock;