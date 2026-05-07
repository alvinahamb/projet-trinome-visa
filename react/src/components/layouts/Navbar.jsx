import React from 'react';
import { NavLink } from 'react-router-dom';
import colors from '@assets/color/color';

/**
 * NavBar - Barre de navigation élégante
 * @param {array} navItems - Liste des éléments de navigation
 *   [{label: '', to: '', badge: ''}]
 */
const NavBar = ({ navItems = [] }) => {
  const navStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '0.25rem',
    fontFamily: "'EB Garamond', Georgia, serif",
    flexWrap: 'nowrap',
    whiteSpace: 'nowrap'
  };

  const navItemStyle = (isActive) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: '0.6rem 1rem',
    borderRadius: '10px',
    textDecoration: 'none',
    position: 'relative',
    backgroundColor: isActive ? `${colors.primary}15` : 'transparent',
    transition: 'all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    flexShrink: 0
  });

  const labelStyle = (isActive) => ({
    fontSize: '1.05rem',
    fontWeight: isActive ? 600 : 500,
    color: isActive ? colors.primary : colors.text.primary,
    transition: 'color 0.3s ease'
  });

  const badgeStyle = {
    position: 'absolute',
    top: '4px',
    right: '4px',
    backgroundColor: colors.action,
    color: colors.white,
    fontSize: '0.65rem',
    fontWeight: 600,
    padding: '2px 6px',
    borderRadius: '10px',
    minWidth: '18px',
    textAlign: 'center'
  };

  const activeIndicatorStyle = {
    position: 'absolute',
    bottom: '0',
    left: '50%',
    transform: 'translateX(-50%)',
    width: '30px',
    height: '3px',
    backgroundColor: colors.primary,
    borderRadius: '3px'
  };

  if (navItems.length === 0) {
    return null;
  }

  return (
    <nav style={navStyle}>
      {navItems.map((item, index) => (
        <NavLink
          key={index}
          to={item.to}
          style={({ isActive }) => navItemStyle(isActive)}
          onMouseEnter={(e) => {
            if (!e.currentTarget.classList.contains('active')) {
              e.currentTarget.style.backgroundColor = colors.gray.light;
            }
          }}
          onMouseLeave={(e) => {
            if (!e.currentTarget.classList.contains('active')) {
              e.currentTarget.style.backgroundColor = 'transparent';
            }
          }}
        >
          {({ isActive }) => (
            <>
              <span style={labelStyle(isActive)}>{item.label}</span>
              {item.badge && (
                <span style={badgeStyle}>{item.badge}</span>
              )}
              {isActive && <div style={activeIndicatorStyle}></div>}
            </>
          )}
        </NavLink>
      ))}
    </nav>
  );
};

export default NavBar;