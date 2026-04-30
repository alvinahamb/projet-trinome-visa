import React, { useState } from 'react';
import colors from '@assets/color/color';

/**
 * Bouton - Composant bouton stylisé avec bordures arrondies
 * Fond blanc, texte et bordure couleur action, au survol inverse les couleurs
 * @param {string} children - Contenu du bouton
 * @param {string} size - Taille du bouton: 'sm', 'md', 'lg'
 * @param {function} onClick - Fonction appelée au clic
 * @param {boolean} disabled - État désactivé
 * @param {string} type - Type du bouton: 'button', 'submit', 'reset'
 * @param {object} style - Styles additionnels
 */
const Bouton = ({ 
  children, 
  size = 'md', 
  onClick, 
  disabled = false,
  type = 'button',
  style = {},
  ...props 
}) => {
  const [isHovered, setIsHovered] = useState(false);

  const sizeStyles = {
    sm: {
      padding: '0.5rem 1rem',
      fontSize: '0.875rem',
      borderRadius: '8px'
    },
    md: {
      padding: '0.75rem 1.5rem',
      fontSize: '1rem',
      borderRadius: '12px'
    },
    lg: {
      padding: '1rem 2rem',
      fontSize: '1.125rem',
      borderRadius: '16px'
    }
  };

  const baseStyle = {
    fontFamily: "'EB Garamond', Georgia, serif",
    fontWeight: 600,
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 0.6 : 1,
    transition: 'all 0.3s ease',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '0.5rem',
    letterSpacing: '0.02em',
    // Style par défaut : fond blanc, texte et bordure action
    backgroundColor: isHovered && !disabled ? colors.action : colors.white,
    color: isHovered && !disabled ? colors.white : colors.action,
    border: `2px solid ${colors.action}`,
    ...sizeStyles[size],
    ...style
  };

  return (
    <button
      type={type}
      style={baseStyle}
      onClick={onClick}
      disabled={disabled}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      {...props}
    >
      {children}
    </button>
  );
};

export default Bouton;