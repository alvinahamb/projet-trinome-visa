import React from 'react';
import { Link } from 'react-router-dom';
import colors from '@assets/color/color';

/**
 * Lien - Composant lien stylisé comme un bouton avec couleur d'action
 * @param {string} children - Contenu du lien
 * @param {string} to - URL de destination
 * @param {string} size - Taille: 'sm', 'md', 'lg'
 * @param {string} variant - Variante: 'filled', 'outline', 'text'
 * @param {object} style - Styles additionnels
 */
const Lien = ({ 
  children, 
  to, 
  size = 'md', 
  variant = 'filled',
  style = {},
  external = false,
  ...props 
}) => {
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

  const variantStyles = {
    filled: {
      backgroundColor: colors.action,
      color: colors.white,
      border: 'none',
      boxShadow: '0 4px 14px rgba(220, 65, 37, 0.25)'
    },
    outline: {
      backgroundColor: 'transparent',
      color: colors.action,
      border: `2px solid ${colors.action}`,
      boxShadow: 'none'
    },
    text: {
      backgroundColor: 'transparent',
      color: colors.action,
      border: 'none',
      boxShadow: 'none',
      padding: '0.25rem 0.5rem',
      textDecoration: 'none'
    }
  };

  const baseStyle = {
    fontFamily: "'EB Garamond', Georgia, serif",
    fontWeight: 600,
    textDecoration: 'none',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '0.5rem',
    letterSpacing: '0.02em',
    transition: 'all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    cursor: 'pointer',
    ...sizeStyles[size],
    ...variantStyles[variant],
    ...style
  };

  const handleMouseEnter = (e) => {
    e.target.style.transform = 'translateY(-2px)';
    if (variant === 'filled') {
      e.target.style.boxShadow = '0 8px 25px rgba(220, 65, 37, 0.35)';
    } else if (variant === 'outline') {
      e.target.style.backgroundColor = `${colors.action}10`;
    } else {
      e.target.style.textDecoration = 'underline';
    }
  };

  const handleMouseLeave = (e) => {
    e.target.style.transform = 'translateY(0)';
    if (variant === 'filled') {
      e.target.style.boxShadow = '0 4px 14px rgba(220, 65, 37, 0.25)';
    } else if (variant === 'outline') {
      e.target.style.backgroundColor = 'transparent';
    } else {
      e.target.style.textDecoration = 'none';
    }
  };

  if (external) {
    return (
      <a
        href={to}
        target="_blank"
        rel="noopener noreferrer"
        style={baseStyle}
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
        {...props}
      >
        {children}
      </a>
    );
  }

  return (
    <Link
      to={to}
      style={baseStyle}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      {...props}
    >
      {children}
    </Link>
  );
};

export default Lien;